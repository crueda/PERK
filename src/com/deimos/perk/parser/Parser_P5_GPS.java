package com.deimos.perk.parser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import com.deimos.perk.kyros.KyrosConnection;
import com.deimos.perk.parser.MessageType_P5_GPS.Type_P5_GPS;
import com.deimos.perk.utils.Utils;

public class Parser_P5_GPS implements Parser{

	private KyrosConnection kyrosConnection = null;
	private Logger log = Logger.getLogger(KyrosConnection.class);

	private final int recordLength = 19;
	
	private int deviceID = -1;
	
	public Parser_P5_GPS() {
		kyrosConnection = new KyrosConnection();
	}

	/**
	 * parse.
	 * @param copyOfRange
	 * @param deviceID
	 */
	@Override
	public void parse(byte[] copyOfRange, int deviceID) {
		this.deviceID = deviceID;
		for (int i = 0; i < (copyOfRange.length)/recordLength; i++) {
			Type_P5_GPS datatype = getType(copyOfRange[i*recordLength]);
			if (datatype == Type_P5_GPS.GPS_TRACK) {
				log.info("Mensaje GPS_TRACK recibido");
				handleTrackPoints(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			else if (datatype == Type_P5_GPS.GPS_FIX_POINT) {
				log.info("Mensaje GPS_FIX_POINT recibido");
				// Mismo tratamiento que handleEM_CARD??? COMPROBAR!!
				handleEM_CARD(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			else if (datatype == Type_P5_GPS.EM_CARD) {
				log.info("Mensaje EM_CARD recibido");
				handleEM_CARD(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			else if (datatype == Type_P5_GPS.LOW_POWER) {
				log.info("Mensaje LOW_POWER recibido. Mandamos LOW BATTERY a Kyros");
				handleLowPower(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			else if (datatype == Type_P5_GPS.MANUAL_ALARM) {
				log.info("Mensaje MANUAL_ALARM recibido. Mandamos ALARM a Kyros");
				handleManualAlarm(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			else if (datatype == Type_P5_GPS.LOCATION_RECORD) {
				log.info("Mensaje LOCATION_RECORD recibido");
				// Mismo tratamiento que GPS_TRACK
				handleTrackPoints(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			//Si hemos creado un socket lo cerramos
			kyrosConnection.closeConnection();
		}
	}
	
	private void handleManualAlarm(byte[] copyOfRange) {
		String guardID = Utils.getHexValue(Arrays.copyOfRange(copyOfRange, 0, 4));
		log.info("GuardID: " + guardID);
		
		kyrosConnection.sendAlarm(deviceID);
	}

	/**
	 * handleTrackPoints. Trata el evento de nueva posicion GPS
	 * @param copyOfRange
	 */
	private void handleTrackPoints(byte[] copyOfRange) {
		try {
//			String guardID = Utils.getHexValue(Arrays.copyOfRange(copyOfRange, 0, 4));
//			log.info("GuardID: " + guardID);
			
			Date date = Utils.getDateGMT0_P5_GPS(Arrays.copyOfRange(copyOfRange, 4, 10));
			
			// Obtenemos la cadena dateGPS con el formato adecuado a la trama GPRMC 
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
			String dateGPS =  sdf.format(date);
			
			// Obtenemos la cadena timeGPS con el formato adecuado a la trama GPRMC
			sdf  = new SimpleDateFormat("HHmmss");
			String timeGPS = sdf.format(date);

			double lat = getCoordinatesGPS(copyOfRange[10], copyOfRange[11], copyOfRange[12], copyOfRange[13]);
			double lon = getCoordinatesGPS(copyOfRange[14], copyOfRange[15], copyOfRange[16], copyOfRange[17]);

			kyrosConnection.sendIMEI(deviceID);
			kyrosConnection.sendMessage(Utils.getTramaGPRMC(dateGPS, timeGPS, lat, lon));
			log.info("Enviamos nueva posicion a Kyros: Lat: "+lat+"; Lon: "+lon+"; Date: "+dateGPS+"; GMT-0: "+timeGPS);
		} catch (Exception e) {
			log.error("Error: " + e);
		}
	}
	
	/**
	 * handleEM_CARD. Trata el evento de lectura de un EM_Card
	 * @param copyOfRange
	 * @param deviceID
	 */
	private void handleEM_CARD(byte[] copyOfRange) {
		try {
//			String guardID = Utils.getHexValue(Arrays.copyOfRange(copyOfRange, 0, 4));
//			log.info("GuardID: " + guardID);
			
			String EM_Card_ID = Utils.getHexValue(Arrays.copyOfRange(copyOfRange, 10, 14));
			Date date = Utils.getDateGMT0_P5_GPS(Arrays.copyOfRange(copyOfRange, 4, 10));
			
			kyrosConnection.sendFixedBeaconToKyros(deviceID, EM_Card_ID, date);
	
		} catch (Exception e) {
			log.error("Error: " + e);
		}
	}

	/**
	 * handleLowPower. Trata el evento de bater�a baja
	 * @param copyOfRange
	 */
	private void handleLowPower(byte[] copyOfRange) {
		int voltaje = 0;
		
		// En estos 3 bytes se supone que viene la informaci�n de la tensi�n de la bater�a
		// La recuperamos, por curiosidad, y la mostramos en el log
		for(int j=0; j<3; j++) {
			int current = copyOfRange[j];
			if(current<0) {
				current = 256 + current;
			}
			voltaje += current;
		}
		
		Date date = Utils.getDateGMT0_P5_GPS(Arrays.copyOfRange(copyOfRange, 4, 10));
		
		// Obtenemos la cadena dateGPS con el formato adecuado a la trama GPRMC 
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		String dateGPS =  sdf.format(date);
		
		// Obtenemos la cadena timeGPS con el formato adecuado a la trama GPRMC
		sdf  = new SimpleDateFormat("HHmmss");
		String timeGPS = sdf.format(date);
		
		double lat = getCoordinatesGPS(copyOfRange[10], copyOfRange[11], copyOfRange[12], copyOfRange[13]);
		double lon = getCoordinatesGPS(copyOfRange[14], copyOfRange[15], copyOfRange[16], copyOfRange[17]);
		
		// Enviamos el mensaje a Kyros y la posici�n
		kyrosConnection.sendIMEI(deviceID);
		kyrosConnection.sendMessage("LOW BATTERY\r\n");
		kyrosConnection.sendMessage(Utils.getTramaGPRMC(dateGPS, timeGPS, lat, lon));
		
		log.info("Low Battery el " + dateGPS + " a las " + timeGPS + ". Volaje: " + voltaje + ". Lat: " + lat + "; Lon: " + lon);
	}

	/**
	 * getCoordinatesGPS. A partir de los bytes recibido obtenemos el valor de la latitud o longitud
	 * @param d1
	 * @param d2
	 * @param d3
	 * @param d4
	 * @return
	 */
	private double getCoordinatesGPS(byte d1, byte d2, byte d3, byte d4) {
		return ((double) (((d1&0xFF) << 24) + ((d2&0xFF) << 16) + ((d3&0xFF) << 8) + (d4&0xFF)) / (1.0E6));
	}

	/**
	 * getType. Devuelve el tipo de mensaje que ha llegado
	 * @param data
	 * @return
	 */
	private Type_P5_GPS getType(byte data) {
		Type_P5_GPS salida = Type_P5_GPS.NONE;

		switch (data) {
		case 65:
			salida = Type_P5_GPS.GPS_TRACK;
			break;

		case 66:
			salida = Type_P5_GPS.GPS_FIX_POINT;
			break;

		case 67:
			salida = Type_P5_GPS.EM_CARD;
			break;

		case 68:
			salida = Type_P5_GPS.LOW_POWER;
			break;

		case 69:
			salida = Type_P5_GPS.MANUAL_ALARM;
			break;

		case 70:
			salida = Type_P5_GPS.LOCATION_RECORD;
			break;

		default:
			salida = Type_P5_GPS.NONE;
			break;
		}

		return salida;
	}
	
}
