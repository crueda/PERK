package com.deimos.perk.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesManager {
	private Properties props;
	private Logger log;
	private static PropertiesManager Instance = null;
	
	private final String KyrosIP = "KyrosIP";
	private final String KyrosPort = "KyrosPort";

	private final String DB_Address = "DB_Address";
	private final String DB_Name = "DB_Name";
	private final String DB_User = "DB_User";
	private final String DB_Password = "DB_Password";
	
	private final String TipoAlerta = "TipoAlerta";
	private final String KyrosEvent = "KyrosEvent";
	
	private final String PortJWM = "PortJWM";
	private final String PortRugby = "PortRugby";
	private final String PortPT = "PortPT";
	
	public PropertiesManager() {
		log = Logger.getLogger(PropertiesManager.class);
		props = new Properties();
		InputStream inStream = null;	
		
		try {
			inStream = new FileInputStream(System.getProperty("user.dir") + "/properties/configuration.properties");
			props.load(inStream);
		} catch (FileNotFoundException e) {
			log.info(e);
		} catch (IOException e) {
			log.info(e);
		}
	}
	
	public static PropertiesManager getInstance() {
		if (Instance == null) {
			Instance = new PropertiesManager();
		}
		return Instance;
	}
	
	public String getDB_Address() {
		return props.getProperty(DB_Address);
	}

	public String getDB_Name() {
		return props.getProperty(DB_Name);
	}

	public String getDB_User() {
		return props.getProperty(DB_User);
	}

	public String getDB_Password() {
		return props.getProperty(DB_Password);
	}

	public String getKyrosIP() {
		return props.getProperty(KyrosIP);
	}
	
	public int getKyrosPort() {
		return (new Integer(props.getProperty(KyrosPort)));
	}
	
	public String getTipoAlerta() {
		return props.getProperty(TipoAlerta);
	}
	
	public String getKyrosEvent() {
		return props.getProperty(KyrosEvent);
	}

	public int getPortJWM() {
		return new Integer(props.getProperty(PortJWM));
	}

	public int getPortRugby() {
		return new Integer(props.getProperty(PortRugby));
	}
	
	public int getPortPT() {
		return new Integer(props.getProperty(PortPT));
	}
}
