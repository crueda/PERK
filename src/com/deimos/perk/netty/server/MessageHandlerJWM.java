package com.deimos.perk.netty.server;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.deimos.perk.parser.Parser;
import com.deimos.perk.parser.Parser_LT_GPRS;
import com.deimos.perk.parser.Parser_P5_GPS;
import com.deimos.perk.utils.Utils;

public class MessageHandlerJWM {
	private byte[] message = null;
	private Logger log = Logger.getLogger(MessageHandlerJWM.class);
	Parser parser = null;

	private enum TipoDispositivo {
		LT_GPRS, P5_GPS, NONE, LT_GPRS_TIME_SINCRO
	}

	public MessageHandlerJWM(byte[] message) {
		this.message = message;
	}

	public int execute() {
		int salida = 0;
		
		try { // an echo server
			int deviceID = 0;
			int datalength = message.length;
			TipoDispositivo tipoDispositivo = null;

			tipoDispositivo = getTipoDispositivo(Arrays.copyOfRange(message, 0, 6));

			if (tipoDispositivo == TipoDispositivo.LT_GPRS) {
				deviceID = getDeviceID(Arrays.copyOfRange(message, 6, 10));

				if(deviceID != -1) {
					log.info("Nueva conexion del ID_Device: " + deviceID);
					parser = new Parser_LT_GPRS(); 
					parser.parse(Arrays.copyOfRange(message, 10, datalength), deviceID);
				} else {
					log.error("El ID del dispositivo no es correcto");
				}

			} else if (tipoDispositivo == TipoDispositivo.P5_GPS) {
				deviceID = getDeviceID(Arrays.copyOfRange(message, 3, 7));

				if(deviceID != -1) {
					log.info("Nueva conexion del ID_Device: " + deviceID);
					parser = new Parser_P5_GPS();
					parser.parse(Arrays.copyOfRange(message, 7, datalength), deviceID);
				} else {
					log.error("El ID del dispositivo no es correcto");
				}

			} else if (tipoDispositivo == TipoDispositivo.LT_GPRS_TIME_SINCRO) { 
				log.info("El dispositivo ha pedido una sincronizaci�n de tiempo"); 
				if(Utils.checkTramaTimeSincro(Arrays.copyOfRange(message, 8, 20))) {
					log.info("La identificacion de la trama de sincronizaci�n es correcta");
					salida = 1;
				}
			} else {
				log.warn("Se ha recibido un mensaje de un dispositivo desconocido");
			}
		} catch (Exception e) {
			log.error("Exception: " + e.getMessage());
		}
		
		return salida;
	}

	/**
	 * getTipoDispositivo. El tipo de dispositivo lo distinguimos por la cabecera que nos llega.
	 * @param copyOfRange
	 * @return TipoDispositivo
	 */
	private TipoDispositivo getTipoDispositivo(byte[] copyOfRange) {
		TipoDispositivo salida = null;

		try {
			if ( ((	copyOfRange[0] == (byte)'*' && 
					copyOfRange[1] == (byte)'W' && 
					copyOfRange[2] == (byte)'M'))) {
				salida = TipoDispositivo.P5_GPS;
			} else if ( ((	copyOfRange[0] == (byte)'#' && 
					copyOfRange[1] == (byte)'J' &&
					copyOfRange[2] == (byte)'W' &&
					copyOfRange[3] == (byte)'M' &&
					copyOfRange[4] == (byte)'L' &&
					copyOfRange[5] == (byte)'t'))) {
				salida = TipoDispositivo.LT_GPRS;
			} else if ( ((	copyOfRange[0] == (byte)'#' && 
					copyOfRange[1] == (byte)'W' &&
					copyOfRange[2] == (byte)'M' &&
					copyOfRange[3] == (byte)'R'))) {
				salida = TipoDispositivo.LT_GPRS_TIME_SINCRO;
			} else {
				salida = TipoDispositivo.NONE;
			}
		} catch (Exception e) {
			log.error("Error: " + e);
		}


		return salida;
	}

	/**
	 * getDeviceID. Obtenemos el ID del dispositivo
	 * @param copyOfRange
	 * @return int
	 */
	private int getDeviceID(byte[] copyOfRange) {
		int salida = -1;

		try {
			for(int i=0; i<4; i++) {
				int current = new Integer(copyOfRange[i]);
				if(current<0) {
					current = 256 + current;
				}
				salida = (int) (salida + current*Math.pow(2, 8*(3-i)));
			}
			// No s� muy bien por qu� hay que a�adir +1, pero lo hago para que coincida
			salida += 1;

		} catch (Exception e) {
			log.error("Error: " + e);
		}

		return salida;
	}

}
