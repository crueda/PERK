package com.deimos.perk.parser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import com.deimos.perk.kyros.KyrosConnection;
import com.deimos.perk.parser.MessageType_LT_GPRS.Type_LT_GPRS;
import com.deimos.perk.utils.Utils;


public class Parser_LT_GPRS implements Parser {
	private Logger log = Logger.getLogger(Parser_LT_GPRS.class);
	private final int recordLength = 24;
	private int deviceID = -1;
	private KyrosConnection kyrosConnection = null;
	
	public Parser_LT_GPRS() {
		kyrosConnection = new KyrosConnection();
	}
	
	@Override
	public void parse(byte[] copyOfRange, int deviceID) {
		this.deviceID = deviceID;
		
		for (int i = 0; i < (copyOfRange.length)/recordLength; i++) {
			Type_LT_GPRS datatype = getType(copyOfRange[i*recordLength]);
			if (datatype == Type_LT_GPRS.NORMAL_RECORD) {
				log.info("NORMAL_RECORD recibido");
				handleNormalRecord(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			else if (datatype == Type_LT_GPRS.ALARM_RECORD) {
				log.info("ALARM_RECORD recibido y enviado a Kyros");
				handleAlarmRecord(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			else if (datatype == Type_LT_GPRS.LOW_POWER) {
				log.info("LOW_POWER recibido y enviado a Kyros");
				handleLowBattery(Arrays.copyOfRange(copyOfRange, 1+i*recordLength, recordLength+i*recordLength));
			}
			else {
				log.debug("Mensaje vac�o recibido");
			}
		}
		//Si hemos creado un socket lo cerramos
		kyrosConnection.closeConnection();
		
	}
	
	private void handleLowBattery(byte[] copyOfRange) {
//		String guardID = Utils.getHexValue(Arrays.copyOfRange(copyOfRange, 6, 12));
//		log.info("GuardID: " + guardID );
		
		kyrosConnection.sendLowBattery(deviceID);
	}

	private void handleAlarmRecord(byte[] copyOfRange) {
//		String guardID = Utils.getHexValue(Arrays.copyOfRange(copyOfRange, 6, 12));
//		log.info("GuardID: " + guardID );
		
		kyrosConnection.sendAlarm(deviceID);
	}

	private void handleNormalRecord(byte[] copyOfRange) {
		
		Date date = Utils.getDateGMT0_LT_GPRS(Arrays.copyOfRange(copyOfRange, 0, 6));
				
		// Obtenemos la cadena dateGPS con el formato adecuado a la trama GPRMC 
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		String dateGPS =  sdf.format(date);
		
		// Obtenemos la cadena timeGPS con el formato adecuado a la trama GPRMC
		sdf  = new SimpleDateFormat("HHmmss");
		String timeGPS = sdf.format(date);
		
//		String guardID = Utils.getHexValue(Arrays.copyOfRange(copyOfRange, 6, 12));
//		log.info("GuardID: " + guardID );
		
		String button = Utils.getHexValue(Arrays.copyOfRange(copyOfRange, 12, 18));
		log.info("Button: " + button + "; Date: "+dateGPS+"; GMT-0: "+timeGPS);
				
		kyrosConnection.sendFixedBeaconToKyros(deviceID, button, date);
	}


	/**
	 * getType. Devuelve el tipo de mensaje que ha llegado
	 * @param data
	 * @return
	 */
	private Type_LT_GPRS getType(byte data) {
		Type_LT_GPRS salida = Type_LT_GPRS.NONE;

		switch (data) {
		case 65:
			salida = Type_LT_GPRS.NORMAL_RECORD;
			break;

		case 66:
			salida = Type_LT_GPRS.ALARM_RECORD;
			break;

		case 67:
			salida = Type_LT_GPRS.LOW_POWER;
			break;
		
		default:
			salida = Type_LT_GPRS.NONE;
			break;
		}

		return salida;
	}
	
}
