package com.deimos.perk.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class Utils {

	private static Logger log = Logger.getLogger(Utils.class);

	/**
	 * getHexValue. Obtenemos la cadena hexadecimal que identifica a un Beacon
	 * @param copyOfRange
	 * @return
	 */
	public static String getHexValue(byte[] copyOfRange) {
		String salida = "";

		try {
			for(int j=0; j<copyOfRange.length; j++) {
				int current = copyOfRange[j];
				if(current<0) {
					current = 256 + current;
				}
				String currentHex = Integer.toHexString(current);
				if(currentHex.length()<2) {
					salida += "0" + currentHex; 
				}
				else {
					salida += currentHex;
				}
			}
		} catch (Exception e) {
			log.error(e);
		}

		return salida;
	}

	/**
	 * getTimeOffsetByTimeZone. Nos devuelve el offset en ms de la zona que le pasemos como argumento.
	 * Esta funcion es necesaria porque las fechas nos vienen con horario chino
	 * @param timeZone
	 * @return
	 */
	public static long getTimeOffsetByTimeZone(String timeZone) {
		// Se obtiene una instacia a un calendario en la maquina local
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));

		return calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
	}

	/**
	 * getTramaGPRMC. Construimos la trama GPRMC que mandaremos a Kyros
	 * @param date
	 * @param time
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static String getTramaGPRMC(String date, String time, double lat,
			double lon) {
		String salida = "";

		try {
			salida = "$GPRMC," + time + ".000,A,";

			int enteroLat = (int) lat;
			double minutosLat = (lat-enteroLat)*60;
			if(Math.abs(enteroLat)<10) {
				salida += "0";
			}
			if(lat<0) {	
				salida += Integer.toString((-1)*enteroLat) + Double.toString((-1)*minutosLat) + ",S,";
			}
			else {
				salida += Integer.toString(enteroLat) + Double.toString(minutosLat) + ",N,";
			}

			int enteroLon = (int) lon;
			double minutosLongitude = (lon-enteroLon)*60;
			if(Math.abs(enteroLon)<100) {
				salida += "0";
			}
			if(Math.abs(enteroLon)<10) {
				salida += "0";
			}
			if(lon<0) {	
				salida += Integer.toString((-1)*enteroLon) + Double.toString((-1)*minutosLongitude) + ",W,";
			}
			else {
				salida += Integer.toString(enteroLon) + Double.toString(minutosLongitude) + ",E,";
			}
			salida += "0.0,0.0,";
			salida += date;
			salida += ",,\r\n";

		} catch (Exception e) {
			log.error("Error: " + e);
		}

		return salida;
	}

	/**
	 * getDateGMT0_LT_GPRS. Devuelve una fecha en GMT0 a partir de los bytes enviado por el JWM
	 * @param copyOfRange
	 * @return
	 */
	public static Date getDateGMT0_LT_GPRS(byte[] copyOfRange) {
		Date date = null;

		try {
			// Ajustamos el calendario con la hora/fecha que recibimos (horario chino)
			Calendar calendar = Calendar.getInstance();
			calendar.set(new Integer("20"+copyOfRange[5]), //year
					(copyOfRange[4] - 1), //month. Para el Calendar January=0 y para los chinos Januay=1
					copyOfRange[3],
					copyOfRange[2],
					copyOfRange[1],
					copyOfRange[0]);

			date = new Date();
			date = calendar.getTime();

			date = cvtToGmt(date);
		} catch (Exception e) {
			log.error(e);
		}

		return date;
	}


	/**
	 * getDateGMT0_P5_GPS. Devuelve una fecha en GMT0 a partir de los bytes enviado por el JWM
	 * @param copyOfRange
	 * @return
	 */
	public static Date getDateGMT0_P5_GPS(byte[] copyOfRange) {
		// Ajustamos el calendario con la hora/fecha que recibimos (horario chino)
		Calendar calendar = Calendar.getInstance();
		calendar.set(new Integer("20"+copyOfRange[0]), //year
				(copyOfRange[1] - 1), //month. Para el Calendar January=0 y para los chinos Januay=1
				copyOfRange[2],
				copyOfRange[3],
				copyOfRange[4],
				copyOfRange[5]);

		// Convertimos la hora china en GMT-0
		// Para ello obtenemos la diferencia en ms del horario chino con el GMT-0  
		long millisOffset = Utils.getTimeOffsetByTimeZone("Asia/Hong_Kong");
		long millis = calendar.getTimeInMillis();
		// Ajustamos el calendario a GMT-0
		calendar.setTimeInMillis(millis - millisOffset);
		Date date = new Date();
		date = calendar.getTime();
		return date;
	}

	/**
	 * cvtToGmt. Convertimos la fecha local en GMT0
	 * @param date
	 * @return
	 */
	private static Date cvtToGmt( Date date ) { 
		Date ret = null;

		try {
			TimeZone tz = TimeZone.getDefault();
			ret = new Date( date.getTime() - tz.getRawOffset() );

			// if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
			if ( tz.inDaylightTime( ret ))
			{
				Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

				// check to make sure we have not crossed back into standard time
				// this happens when we are on the cusp of DST (7pm the day before the change for PDT)
				if ( tz.inDaylightTime( dstDate ))
				{
					ret = dstDate;
				}
			}

		} catch (Exception e) {
			log.error(e);
		}

		return ret;
	}

	/**
	 * checkTramaTimeSincro. Comprobamos que se recibe la identifiaci�n de la trama correctamente
	 * Seg�n la documentaci�n. Identification: 55+55+55+55+55+55+AA+AA+AA+AA+AA+AA
	 * En la pr�ctica, en decimal recibimos 85 y -86
	 * @param copyOfRange
	 * @return
	 */
	public static boolean checkTramaTimeSincro(byte[] copyOfRange) {
		boolean salida = false;

		try {
			if(	copyOfRange[0] == 85 && copyOfRange[1] == 85 && 
				copyOfRange[2] == 85 && copyOfRange[3] == 85 &&
				copyOfRange[4] == 85 && copyOfRange[5] == 85 && 
				copyOfRange[6] == -86 && copyOfRange[7] == -86 &&
				copyOfRange[8] == -86 && copyOfRange[9] == -86 &&
				copyOfRange[10] == -86 && copyOfRange[11] == -86) {
				salida = true;
			}
		} catch (Exception e) {
			log.error(e);
		}

		return salida;
	}
	
	
	public static byte[] GetTimeFrameToSincro(Date time){
		byte tm[] = new byte[8];
		
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			
			 tm[0]=0x55;
			 tm[1]=(byte) 0xCC;
			 tm[2]=(byte) ( ((calendar.get(Calendar.YEAR)%100)/10)*16+((calendar.get(Calendar.YEAR)%100)%10));
			 tm[3]=(byte) ( ((calendar.get(Calendar.MONTH)+1) /10)*16+((calendar.get(Calendar.MONTH)+1)%10));
			 tm[4]=(byte) ( ( calendar.get(Calendar.DATE)/10)*16+(calendar.get(Calendar.DATE)%10));
			 tm[5]=(byte) ((calendar.get(Calendar.HOUR_OF_DAY)/10)*16+(calendar.get(Calendar.HOUR_OF_DAY)%10));
			 tm[6]=(byte) ((calendar.get(Calendar.MINUTE)/10)*16+(calendar.get(Calendar.MINUTE)%10));
			 tm[7]=(byte) ((calendar.get(Calendar.SECOND)/10)*16+(calendar.get(Calendar.SECOND)%10));
		} catch (Exception e) {
			log.error(e);
		}
				
		return tm;
	}
}