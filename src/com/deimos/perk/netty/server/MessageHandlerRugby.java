package com.deimos.perk.netty.server;


public class MessageHandlerRugby {
	private String message = "";
	
	public MessageHandlerRugby(String message) {
		this.message = message;
	}

	public String execute() {
		String salida = "";
		String[] datos = new String[4];

		try {
		datos = getDatos(message);
		
		if(datos[0].equalsIgnoreCase("E1"))
		{
					System.out.println("Recibida trama E1. Enviar respuesta: " + datos[1]);
					// Mensaje de respuesta al equipo
					salida = "RE1;"+datos[1];
		}
		else if(datos[0].equalsIgnoreCase("E3"))
		{
					System.out.println("Recibida trama E3. Enviar respuesta: " + datos[1]);
					salida = "RE3;"+datos[1];
		}
		else if(datos[0].equalsIgnoreCase("E4"))
		{
					System.out.println("Recibida trama E4. Enviar respuesta: " + datos[1]);
					salida = "RE4;"+datos[1];
		}
		//IAGO - Este caso no lo he comprobado
		else if(datos[0].equalsIgnoreCase("E2"))
		{
					System.out.println("Recibida trama E2. Enviar respuesta: " + datos[1]);
					salida = "RE2;"+datos[1];
		}
		else {
			System.out.println("Recibida otra trama. Enviar respuesta: " + datos[1]);
			// Mensaje de respuesta al equipo
			salida = "R"+datos[0]+";"+datos[1];
		}
		} catch (Exception e) {
			System.out.println("Exception en execute: " + e);
		}
		salida += "\r\n";
		return salida;
	}
	
	/*
	 * datos[0] = E1(lectura de un iButton)/E2(apertura del equipo)/E3(start)/E4(keep alive)
	 * datos[1] = #seq
	 * datos[2] = IMEI
	 * datos[3] = MAC_Baliza (solo para E1)
	 */
	private static String[] getDatos(String cadenaRecivida) {
		String [] resultado = new String[4];
		int aux;
		//String cuartoCampo ="";
		aux = cadenaRecivida.indexOf(";");
		if (aux != -1)
			resultado[0] = cadenaRecivida.substring(0, aux);
		else {
			resultado[0]=resultado[1]=resultado[2]=resultado[3]="";
			return resultado;
		}
		cadenaRecivida = cadenaRecivida.substring(cadenaRecivida.indexOf(";")+1,cadenaRecivida.length());
		resultado[1] = cadenaRecivida.substring(0,cadenaRecivida.indexOf(";"));
		cadenaRecivida = cadenaRecivida.substring(cadenaRecivida.indexOf(";")+1,cadenaRecivida.length());
		resultado[2] = cadenaRecivida.substring(0,cadenaRecivida.indexOf(";"));
		cadenaRecivida = cadenaRecivida.substring(cadenaRecivida.indexOf(";")+1,cadenaRecivida.length());
		cadenaRecivida = cadenaRecivida.substring(cadenaRecivida.indexOf(";")+1,cadenaRecivida.length());
		resultado[3] = cadenaRecivida.substring(0,12);
		
		return resultado;
	}

}
