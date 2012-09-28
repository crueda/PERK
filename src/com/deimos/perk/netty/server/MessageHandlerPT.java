package com.deimos.perk.netty.server;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer; 

import com.deimos.perk.kyros.KyrosConnection;
import com.deimos.perk.utils.Utils;

public class MessageHandlerPT {
	private String message = "";
	private KyrosConnection kyrosConnection = null;
	
	public MessageHandlerPT(String message) {
		this.message = message;
	}

	public String execute() {
		String salida = "";
		String delim = ",";
		String strToken = "";
		String strLat = "";
		String strLon = "";
		
		System.out.println("Recibida trama Picotrack: " + message);

		/*StringTokenizer st = new StringTokenizer(message, delim);
		
		int index = 0;
		while (st.hasMoreTokens()) {
			strToken = st.nextToken();
		    System.out.println(index + ": " + strToken);
		    index++;
		    if (index==4)
		    	strLat = strToken;
		    else if (index==5)
		    	strLon = strToken;
		    
		}
		*/
		
		
		try{
			/*
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
*/
			
		} catch (Exception e) {
			System.out.println("Exception en execute: " + e);
		}
		salida += "\r\n";
		return salida;
	}
	
	
	

	
	

}
