package com.deimos.perk.accessBD;

public class BeaconDevice {
	private double LatitudeInt;
	private double LatitudeDec;
	private double LongitudeInt;
	private double LongitudeDec;
	private int Cartography;
	
	public BeaconDevice() {
	}

	public double getLatitudeInt() {
		return LatitudeInt;
	}

	public void setLatitudeInt(double latitudeInt) {
		LatitudeInt = latitudeInt;
	}

	public double getLatitudeDec() {
		return LatitudeDec;
	}

	public void setLatitudeDec(double latitudeDec) {
		LatitudeDec = latitudeDec;
	}

	public double getLongitudeInt() {
		return LongitudeInt;
	}

	public void setLongitudeInt(double longitudeInt) {
		LongitudeInt = longitudeInt;
	}

	public double getLongitudeDec() {
		return LongitudeDec;
	}

	public void setLongitudeDec(double longitudeDec) {
		LongitudeDec = longitudeDec;
	}

	public void setCartography(int cartography) {
		Cartography = cartography;
	}
	
	public int getCartography() {
		return Cartography;
	}
}
