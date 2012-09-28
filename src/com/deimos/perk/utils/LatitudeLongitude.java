package com.deimos.perk.utils;

import java.io.Serializable;

import drasys.or.geom.GeomException;
import drasys.or.geom.geo.proj.Mercator;

/** 
 * Class that models a LatLon position.
 */
public class LatitudeLongitude implements Serializable {

	static final long serialVersionUID = 1L;
	
    /** Latitude at degrees. */
    private double latitude;

    /** Longitude at degrees. */
    private double longitude;

    /** Initialitation state. */
    private boolean init = false;

    /** Creates a new instance of LatitudeLongitude. */
    public LatitudeLongitude()  {
        init = false;
    }

    /** Creates a new instance of LatitudLongitud.
     * @param _latitude Latitude Value
     * @param _longitude Longitude Value
     */
    public LatitudeLongitude(double latitudeParam, double longitudeParam) {
        latitude = latitudeParam;
        longitude = longitudeParam;
        init = true;
    }

    /** Creates a new instance of LatitudeLongitude.
     * @param degreesLatitude Latitude degrees value
     * @param minutesLatitude Latitude minutes value
     * @param secondsLatitude Latitude seconds value
     * @param degreesLongitude Longitude degrees value
     * @param secondsLongitude Longitude seconds value
     * @param minutesLongitude Longitude minutes value
     */
    public LatitudeLongitude(double degreesLatitude, double minutesLatitude, double secondsLatitude, 
    		double degreesLongitude, double minutesLongitude, double secondsLongitude) {
        double mlattemp, slattemp, mlontemp, slontemp;
        mlattemp = (minutesLatitude) / 60.0;
        mlontemp = (minutesLongitude) / 60.0;
        slattemp = (secondsLatitude) / 3600.0;
        slontemp = (secondsLongitude) / 3600.0;
        latitude = degreesLatitude + mlattemp + slattemp;
        longitude = degreesLongitude + mlontemp + slontemp;
        init = true;
    }
    
    /** Creates a new instance of LatitudLongitud. 
     * @param x X Value
     * @param y Y Value
     */    
    public LatitudeLongitude(String x, String y) {
    
    	drasys.or.geom.rect2.Point p;
    	drasys.or.geom.geo.Point latLon = null;

		Mercator m = new Mercator(0);
		m.setEllipsoid(drasys.or.geom.geo.Ellipsoid.getInstance("WGS 84"));
		p = new drasys.or.geom.rect2.Point(Double.valueOf(x), Double.valueOf(y));
		try {
			latLon = (drasys.or.geom.geo.Point) m.inverse(p);
		} catch (GeomException e) {
			e.getStackTrace();
		}
		
		if (latLon != null) {
			latitude = latLon.latitude();
			longitude = latLon.longitude();
			init = true;
		}

    }
    
    /** Method for setting longitude value
     * @param latitudeParam Latitude value at degrees
     */    
    public void setLatitude(double latitudeParam) {
        latitude = latitudeParam;
    }
    
    /** Method for setting longitude
     * @param longitudeParam Longitude value at degrees
     */    
    public void setLongitude(double longitudeParam) {
        longitude = longitudeParam;
    }
    
    /** Method for getting latitude
     * @return Latitude value
     */    
    public double getLatitude() {
        return latitude;
    }
    
    /** Method for getting Longitude
     * @return Longitude value
     */    
    public double getLongitude() {
        return longitude;
    }
    
    public int getDegLatitude() {
    	double lat = this.latitude;
    	if (lat < 0) {
			return -1 * ((int) java.lang.Math.floor(java.lang.Math.abs(lat)));
		} else {
			return ((int) java.lang.Math.floor(lat));
		}
    }
    
    public int getDegLongitude() {
    	double lon = this.longitude;
    	if (lon < 0) {
			return -1 * ((int) java.lang.Math.floor(java.lang.Math.abs(lon)));
		} else {
			return ((int) java.lang.Math.floor(lon));
		}
    }
    
    public int getMinLatitude() {
        double minutes;
    	double lat = this.latitude;

        minutes = java.lang.Math.floor((java.lang.Math.abs(lat) 
        		- java.lang.Math.floor(java.lang.Math.abs(lat))) * 60);
		if (this.latitude < 0) {
			return -1 * (int) minutes;
		} else {
			return (int) minutes;
		}
    }
    
    public int getMinLongitude() {
        double minutes;
    	double lon = this.longitude;
    	
        minutes = java.lang.Math.floor((java.lang.Math.abs(lon) 
        		- java.lang.Math.floor(java.lang.Math.abs(lon))) * 60);
		if (this.longitude < 0) {
			return -1 * (int) minutes;
		} else {
			return (int) minutes;
		}
    }
    
    public double getSegLatitude() {
        double minutes;
        double seconds;
    	double lat = this.latitude;

        minutes = (java.lang.Math.abs(lat) - java.lang.Math.floor(java.lang.Math.abs(lat))) * 60;
        seconds = (minutes - java.lang.Math.floor(minutes)) * 60;
		if (this.latitude < 0) {
			return -1 * seconds;
		} else {
			return seconds;
		}
    }
    
    public double getSegLongitude() {
        double minutes;
        double seconds;
    	double lon = this.longitude;

        minutes = (java.lang.Math.abs(lon) - java.lang.Math.floor(java.lang.Math.abs(lon))) * 60;
        seconds = (minutes - java.lang.Math.floor(minutes)) * 60;
		if (this.longitude < 0) {
			return -1 * seconds;
		} else {
			return seconds;
		}
    }
    
    /** Returns if the LatitudeLongitude object is initialized with correct values or
     * not
     * @return True if the LatitudeLongitude object is initialized with correct values, false
     * otherwishe
     *
     */    
    public boolean isInitialized() {
        return init;
    }
    
    /** Converts to string a LatitudeLongitude object
     * @return An description string for this LongitudeLatitude
     */    
    public String toString() {
        return new String(" LatitudeLongitude: Latitude:" + this.latitude + " Longitude:" + this.longitude + " \n");
    }
    
}

