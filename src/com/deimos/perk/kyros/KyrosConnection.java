package com.deimos.perk.kyros;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.deimos.perk.accessBD.AccessBD;
import com.deimos.perk.accessBD.BeaconDevice;
import com.deimos.perk.accessBD.Driver;
import com.deimos.perk.utils.PropertiesManager;
import com.deimos.perk.utils.Utils;
import com.deimos.perk.wsclient.WiloctWSTagTracking;
import com.deimos.perk.wsclient.WiloctWSTagTrackingPortType;

public class KyrosConnection {
	private Socket socket = null;
	private PrintWriter out = null;

	private Logger log = Logger.getLogger(KyrosConnection.class);

	public KyrosConnection() {
	}

	public boolean sendIMEI(int DeviceID) {
		boolean salida = false;

		try {
			socket = new Socket(PropertiesManager.getInstance().getKyrosIP(),
					PropertiesManager.getInstance().getKyrosPort());
			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);

			out.println("IMEI="+Integer.toString(DeviceID)+"\r\n");
			out.flush();
			salida = true;
		} catch (Exception e) {
			log.error(e);
		}

		return salida;
	}

	public boolean sendMessage(String message) {
		boolean salida = false;

		try {
			out.println(message);
			out.flush();
			salida = true;
		} catch (Exception e) {
			log.error(e);
		}

		return salida;
	}

	public void sendFixedBeaconToKyros(int deviceID, String button, Date date) {
		try {
			AccessBD accessBD = new AccessBD(
					PropertiesManager.getInstance().getDB_Address(),
					PropertiesManager.getInstance().getDB_Name(),
					PropertiesManager.getInstance().getDB_User(),
					PropertiesManager.getInstance().getDB_Password());

			Driver driver = accessBD.getDriver(button);
			
			if(driver == null) {
				BeaconDevice beaconDevice = new BeaconDevice();
				beaconDevice = accessBD.getBeaconDevice(button);

				if(beaconDevice != null) {
					if(beaconDevice.getCartography() == 3) {

						/**
						 * Si mandasemos una trama INDOOR_BEACON perder�amos la fecha/hora
						 */
						//					sendIMEI(deviceID);		
						//					String tramaIndoor = "INDOOR_BEACON,"+button+",10\r\n";
						//					sendMessage(tramaIndoor);
						//					log.info("Mandamos trama indoor a Kyros: " + tramaIndoor);

						//Manadamos la posici�n a trav�s de un WS 
						Long dateTime = date.getTime();
						Double latitude = beaconDevice.getLatitudeInt() + beaconDevice.getLatitudeDec()/60;
						Double longitude = beaconDevice.getLongitudeInt() + beaconDevice.getLongitudeDec()/60;
						int mapZone = 1;
						Integer alertCode = 0;
						String alertDescription = "";

						WiloctWSTagTracking client = new WiloctWSTagTracking();
						WiloctWSTagTrackingPortType service = client.getWiloctWSTagTrackingHttpPort();
						int result = service.addTagTracking(Integer.toString(deviceID) , dateTime, latitude, longitude, mapZone, alertCode, alertDescription);
						if (result == 0) {
							log.info("WS con " + deviceID + "/" + button + " a Kyros");
						}
						else {
							log.warn("Problema al llamar al WS para insertar nueva posicion de " + button);
						}
					} else if (beaconDevice.getCartography() == 1) {
						//Es una posici�n outdoor fija
						Double latitude = beaconDevice.getLatitudeInt() + beaconDevice.getLatitudeDec()/60;
						Double longitude = beaconDevice.getLongitudeInt() + beaconDevice.getLongitudeDec()/60;

						// Obtenemos la cadena dateGPS con el formato adecuado a la trama GPRMC 
						SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
						String dateGPS =  sdf.format(date);

						// Obtenemos la cadena timeGPS con el formato adecuado a la trama GPRMC
						sdf  = new SimpleDateFormat("HHmmss");
						String timeGPS = sdf.format(date);

						String tramaGPRMC = Utils.getTramaGPRMC(dateGPS, timeGPS, latitude, longitude);
						sendIMEI(deviceID);
						sendMessage(PropertiesManager.getInstance().getKyrosEvent() + "\r\n");
						sendMessage(tramaGPRMC);
						closeConnection();
						log.info("Mandamos trama GPRMC a Kyros: " + tramaGPRMC);
					} else {
						log.error("Problema con la cartograf�a de " + button);
					}
				} 
				else {
					log.error("No se ha encontrado " + button + " en la base de datos. Ni vigilante ni beacon");
				}
			//Se trata de un nuevo vigilante que se identifica
			} else {
				log.info("Cambiamos al vigilante " + driver.getFirstName() + " " + driver.getLastName());
				sendIMEI(deviceID);
				sendMessage("DRIVER," + button + "\r\n");
				closeConnection();
			}
			

		} catch (Exception e) {
			log.error(e);
		}

	}

	public boolean closeConnection() {
		boolean salida = false;

		try {
			if (out != null) {
				out.close();
			}
			salida = true;
		} catch (Exception e) {
			log.error(e);
		}

		return salida;	
	}

	public void sendAlarm(int deviceID) {
		try {
			sendIMEI(deviceID);
			sendMessage(PropertiesManager.getInstance().getTipoAlerta()+"\r\n");
		} catch (Exception e) {
			log.error(e);
		}

	}

	public void sendLowBattery(int deviceID) {
		try {
			sendIMEI(deviceID);
			sendMessage("LOW BATTERY\r\n");
		} catch (Exception e) {
			log.error(e);
		}		
	}
}
