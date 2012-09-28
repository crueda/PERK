package com.deimos.perk.utils.geocoding;

import java.util.List;



/** Class that represents the response returned by Google.
 * @author jcap
 */
public class GoogleGeocodingResponse {
    
	public static final String TYPE_STREET_ADDRESS          	 =  "street_address";
	public static final String TYPE_ROUTE          		 		 =  "route";
	
	public static final String QUALITY_ROOF_TOP           		 = "ROOFTOP"; 
	public static final String QUALITY_RANGE_INTERPOLATED 		 = "RANGE_INTERPOLATED";
	public static final String QUALITY_GEOMETRIC_CENTER   		 = "GEOMETRIC_CENTER";
	public static final String QUALITY_APPROXIMATE        		 = "APPROXIMATE";
	
    /** Response is ok. */
    private boolean isOk;
    
    private String statusMsg;
    
    /** Bottomright georeference's Coord. Y */    
    private double yBottomRight;
    
    /** Bottomright georeference's Coord. X */    
    private double xBottomRight;
    
    /** Upperleft georeference's Coord. Y */    
    private double yUpperLeft;
    
    /** Upperleft georeference's Coord. X */    
    private double xUpperLeft;
     
    //viewport
    private double viewportNortheastLat;
    
    //viewport
    private double viewportNortheastLng;
    
    //viewport
    private double viewportSouthwestLat;
    
    //viewport
    private double viewportSouthwestLng;
     
    //bounds
    private double boundsNortheastLat;
    
    //bounds
    private double boundsNortheastLng;
    
    //bounds
    private double boundsSouthwestLat;
    
    //bounds
    private double boundsSouthwestLng;
    
    /** List of Geocoding. */    
    private java.util.List<GeocodingDto> geocodingList;
    
    /** Error String. */    
    private String strError;
    
    /** Scale. */    
    private int scale;
    
    /** Creates a new instance of GeogramaMapResponse. */
    public GoogleGeocodingResponse() {
    }   

    public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public double getyBottomRight() {
		return yBottomRight;
	}

	public void setyBottomRight(double yBottomRight) {
		this.yBottomRight = yBottomRight;
	}

	public double getxBottomRight() {
		return xBottomRight;
	}

	public void setxBottomRight(double xBottomRight) {
		this.xBottomRight = xBottomRight;
	}

	public double getyUpperLeft() {
		return yUpperLeft;
	}

	public void setyUpperLeft(double yUpperLeft) {
		this.yUpperLeft = yUpperLeft;
	}

	public double getxUpperLeft() {
		return xUpperLeft;
	}

	public void setxUpperLeft(double xUpperLeft) {
		this.xUpperLeft = xUpperLeft;
	}

	public double getViewportNortheastLat() {
		return viewportNortheastLat;
	}

	public void setViewportNortheastLat(double viewportNortheastLat) {
		this.viewportNortheastLat = viewportNortheastLat;
	}

	public double getViewportNortheastLng() {
		return viewportNortheastLng;
	}

	public void setViewportNortheastLng(double viewportNortheastLng) {
		this.viewportNortheastLng = viewportNortheastLng;
	}

	public double getViewportSouthwestLat() {
		return viewportSouthwestLat;
	}

	public void setViewportSouthwestLat(double viewportSouthwestLat) {
		this.viewportSouthwestLat = viewportSouthwestLat;
	}

	public double getViewportSouthwestLng() {
		return viewportSouthwestLng;
	}

	public void setViewportSouthwestLng(double viewportSouthwestLng) {
		this.viewportSouthwestLng = viewportSouthwestLng;
	}

	public double getBoundsNortheastLat() {
		return boundsNortheastLat;
	}

	public void setBoundsNortheastLat(double boundsNortheastLat) {
		this.boundsNortheastLat = boundsNortheastLat;
	}

	public double getBoundsNortheastLng() {
		return boundsNortheastLng;
	}

	public void setBoundsNortheastLng(double boundsNortheastLng) {
		this.boundsNortheastLng = boundsNortheastLng;
	}

	public double getBoundsSouthwestLat() {
		return boundsSouthwestLat;
	}

	public void setBoundsSouthwestLat(double boundsSouthwestLat) {
		this.boundsSouthwestLat = boundsSouthwestLat;
	}

	public double getBoundsSouthwestLng() {
		return boundsSouthwestLng;
	}

	public void setBoundsSouthwestLng(double boundsSouthwestLng) {
		this.boundsSouthwestLng = boundsSouthwestLng;
	}

	public List<GeocodingDto> getGeocodingList() {
		return geocodingList;
	}

	public void setGeocodingList(List<GeocodingDto> geocodingList) {
		this.geocodingList = geocodingList;
	}

	public String getStrError() {
		return strError;
	}

	public void setStrError(String strError) {
		this.strError = strError;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
     * Nos devuelve el GeocodingDto segun el tipo de resultado que solicitemos o el primer resultado si no hay resultado con ese valor
     * @param type
     * @return
     */
    public GeocodingDto getGeocodingDto(String type) {
    	GeocodingDto dto = null;
    	if ((geocodingList != null) && (!geocodingList.isEmpty())) {
    		for (int i = 0; i < geocodingList.size(); i++) {
    			dto = (GeocodingDto) geocodingList.get(i);
    			if (dto.getType().equalsIgnoreCase(type)) {
    				break;
    			}
    		}
    		if (!dto.getType().equalsIgnoreCase(type)) {
    			dto = (GeocodingDto) geocodingList.get(0);
    		}
    	}
    	return dto;
    }
    
    /**
     * Nos devuelve el GeocodingDto segun la calidad de Latitud y Longitud proporcionada o el primer resultado si no hay con esa calidad
     * @param quality
     * @return
     */
    public GeocodingDto getGeocodingDtoByQuality(String quality) {
    	GeocodingDto dto = null;
    	if ((geocodingList != null) && (!geocodingList.isEmpty())) {
    		for (int i = 0; i < geocodingList.size(); i++) {
    			dto = (GeocodingDto) geocodingList.get(i);
    			if (dto.getQualitLatlng().equalsIgnoreCase(quality)) {
    				break;
    			}
    		}
    		if (!dto.getQualitLatlng().equalsIgnoreCase(quality)) {
    			dto = (GeocodingDto) geocodingList.get(0);
    		}
    	}
    	return dto;
    }
    
    /**
     * Devuelve el mejor resultado devuelto por el geocoding de google. Segun la documentacion los resultados son devueltos ordenados
     * de mayor a menor precision.
     * @return
     */
    public GeocodingDto getBestGeocodingDto() {
    	GeocodingDto dto = null;
    	if ((geocodingList != null) && (!geocodingList.isEmpty())) {
    		dto = (GeocodingDto) geocodingList.get(0);
    	}
    	return dto;
    }
    
}
