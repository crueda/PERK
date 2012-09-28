package com.deimos.perk.accessBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

//http://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html

public class AccessBD {
	Connection connection;
	private boolean connectionFree = true;
	String state = "";
	int imei_count, name_count, telephone_count, n;
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	Object[] sinoatodo = {"Si", "Si a todo", "No", "No a todo"};
	Logger log = Logger.getLogger(AccessBD.class);

	public AccessBD (String ip_, String bd_, String usuario_, String clave_) throws Exception {
		String driver = "jdbc:mysql://"+ip_+":3306/"+bd_;
		String usuario = usuario_;
		String clave = clave_;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(driver,usuario,clave); 
		}catch(SQLException e){
			log.info("Error al conectar con la base de datos: "+e);
			state = e.getMessage();
		}
	}

	protected synchronized Connection getConnection () {
		while (connectionFree == false) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				log.info("InterruptedException get: "+e);
			}
		}
		connectionFree = false;
		notify();
		return connection;
	}

	protected synchronized void releaseConnection () {
		while (connectionFree == true) {
			try {
				wait ();
			}
			catch (InterruptedException e) {
				log.info("InterruptedException release: "+e);
			}
		}
		connectionFree = true;
		notify();
	}

	public String getState(){
		return state;
	}

	public void resetState(){
		state = "";
	}

	public BeaconDevice getBeaconDevice(String beacon) {
		BeaconDevice beaconLocation = null;
		try {
			getConnection();
			String query = "SELECT POS_LATITUDE_DEGREE, POS_LATITUDE_MIN, POS_LONGITUDE_DEGREE, POS_LONGITUDE_MIN, CARTOGRAPHY_LAYER_ID FROM BEACON_INDOOR WHERE MAC_ADRESS=?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, beacon);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				beaconLocation = new BeaconDevice();
				beaconLocation.setLatitudeInt(resultSet.getDouble((1)));
				beaconLocation.setLatitudeDec(resultSet.getDouble((2)));
				beaconLocation.setLongitudeInt(resultSet.getDouble((3)));
				beaconLocation.setLongitudeDec(resultSet.getDouble((4)));
				beaconLocation.setCartography(resultSet.getInt((5)));
			}
		} catch (SQLException e) {
			log.info("Could not get the beaconLocation " + e.toString());
			state = e.getMessage();
			beaconLocation = null;
		}finally{
			releaseConnection();
		}
		return beaconLocation;
	}

	public Driver getDriver(String id) {
		Driver driver = null;
		try {
			getConnection();
			String query = "SELECT FIRST_NAME, LAST_NAME FROM DRIVER WHERE ID_TAG=?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				driver = new Driver();
				driver.setFirstName(resultSet.getString((1)));
				driver.setLastName(resultSet.getString((2)));
			}
		} catch (SQLException e) {
			log.info("Could not get the driver " + e.toString());
			state = e.getMessage();
			driver = null;
		}finally{
			releaseConnection();
		}
		return driver;
	}
}
