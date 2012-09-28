package com.deimos.perk.utils.geocoding;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;




/**
 * GoogleGeocodingResponseParser.
 *  Parser for response of Google geocoding.
 */

public class GoogleGeocodingResponseParser {
	
	private static final String STATUS_XPATH        				=  "/GeocodeResponse/status/text()";
	private static final String RESULTS_XPATH       				=  "/GeocodeResponse/result";
	private static final String TYPE_XPATH          		 		=  "/type/text()";
	private static final String STREET_NUMBER_XPATH 		 		=  "/address_component[type/text()='street_number']/long_name/text()";
	private static final String STREET_XPATH       			 		=  "/address_component[type/text()='route']/long_name/text()";
	private static final String LOCALITY_XPATH       		 		=  "/address_component[type/text()='locality']/long_name/text()";
	private static final String MUNICIPALITY_XPATH       	     	=  "/address_component[type/text()='administrative_area_level_3']/long_name/text()";
	private static final String PROVINCIE_XPATH       	     		=  "/address_component[type/text()='administrative_area_level_2']/long_name/text()";
	private static final String STATE_XPATH       		     		=  "/address_component[type/text()='administrative_area_level_1']/long_name/text()";
	private static final String COUNTRY_XPATH       		 		=  "/address_component[type/text()='country']/long_name/text()";
	private static final String CP_XPATH         			 		=  "/address_component[type/text()='postal_code']/long_name/text()";
	private static final String LAT_XPATH        			 		=  "/geometry/location/lat/text()";
	private static final String LNG_XPATH  					 		=  "/geometry/location/lng/text()";
	private static final String QUALITY_LATLNG_XPATH 		 		=  "/geometry/location_type/text()";
	private static final String VIEWPORT_SOUTHWEST_LAT_XPATH 		=  "/geometry/viewport/southwest/lat/text()";
	private static final String VIEWPORT_SOUTHWEST_LNG_XPATH 		=  "/geometry/viewport/southwest/lng/text()";
	private static final String VIEWPORT_NORTHEAST_LAT_XPATH 		=  "/geometry/viewport/northeast/lat/text()";
	private static final String VIEWPORT_NORTHEAST_LNG_XPATH 		=  "/geometry/viewport/northeast/lng/text()";
	private static final String BOUNDS_SOUTHWEST_LAT_XPATH   		=  "/geometry/bounds/southwest/lat/text()";
	private static final String BOUNDS_SOUTHWEST_LNG_XPATH  		=  "/geometry/bounds/southwest/lng/text()";
	private static final String BOUNDS_NORTHEAST_LAT_XPATH   		=  "/geometry/bounds/northeast/lat/text()";
	private static final String BOUNDS_NORTHEAST_LNG_XPATH   		=  "/geometry/bounds/northeast/lng/text()";
	
	private static final String STATUS_OK = "OK"; //geocoding realizado con exito y al menos con un resultado
	private Document geocoderResultDocument = null;
	private static final Logger LOG = Logger.getLogger(GoogleGeocodingResponseParser.class.getName());

	/**
	 * GoogleGeocodingResponseParser.
	 * @param doc Document
	 */
	public GoogleGeocodingResponseParser(Document doc) {
		this.geocoderResultDocument = doc;
	}

	/**
	 * parser.
	 * @param geocodingResponse GoogleGeocodingResponse
	 * @return GoogleGeocodingResponse
	 */
	public GoogleGeocodingResponse parser(GoogleGeocodingResponse geocodingResponse) {
		NodeList result = null;
		XPathFactory factory;
		XPath xpath;
		String value;
		String path;
		List<GeocodingDto> geocodingList = new ArrayList<GeocodingDto>();

		factory = XPathFactory.newInstance();
		xpath = factory.newXPath();

		try {
			result = (NodeList) xpath.evaluate(STATUS_XPATH, geocoderResultDocument, XPathConstants.NODESET);

			if (valid(result)) {
				value = result.item(0).getNodeValue();

				if (value.equalsIgnoreCase(STATUS_OK)) {
					geocodingResponse.setOk(true);
					geocodingResponse.setStatusMsg(STATUS_OK);
					//obtenemos todos los resultados proporcionados por el geocoding
					result = (NodeList) xpath.evaluate(RESULTS_XPATH , geocoderResultDocument, XPathConstants.NODESET);
					if (valid(result)) {
						//Hay por lo menos un resultado
						//TYPE_STREET_ADDRESS
						for (int i = 0; i < result.getLength(); i++) {
							//Guardo toda la informacion en una posicion de la lista
					        GeocodingDto geocodingDto = new GeocodingDto();

							path = "GeocodeResponse/result[" + (i + 1) + "]";
							getValue(geocodingDto, geocodingResponse, path, TYPE_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, STREET_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, STREET_NUMBER_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, LOCALITY_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, STATE_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, COUNTRY_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, CP_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, PROVINCIE_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, MUNICIPALITY_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, LAT_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, LNG_XPATH, xpath);
							getValue(geocodingDto, geocodingResponse, path, QUALITY_LATLNG_XPATH, xpath);							
							
							//Estos valores solo los cojo del primer result ya que es el que mas informacion proporciona y por lo tanto el mas exacto
							if (i == 0) {
								getValue(geocodingDto, geocodingResponse, path, VIEWPORT_SOUTHWEST_LAT_XPATH, xpath);
								getValue(geocodingDto, geocodingResponse, path, VIEWPORT_SOUTHWEST_LNG_XPATH, xpath);									
								getValue(geocodingDto, geocodingResponse, path, VIEWPORT_NORTHEAST_LAT_XPATH, xpath);								
								getValue(geocodingDto, geocodingResponse, path, VIEWPORT_NORTHEAST_LNG_XPATH, xpath);								
								getValue(geocodingDto, geocodingResponse, path, BOUNDS_SOUTHWEST_LAT_XPATH, xpath);								
								getValue(geocodingDto, geocodingResponse, path, BOUNDS_SOUTHWEST_LNG_XPATH, xpath);								
								getValue(geocodingDto, geocodingResponse, path, BOUNDS_NORTHEAST_LAT_XPATH, xpath);								
								getValue(geocodingDto, geocodingResponse, path, BOUNDS_NORTHEAST_LNG_XPATH, xpath);								
							}
							geocodingList.add(geocodingDto);
						}
						geocodingResponse.setGeocodingList(geocodingList);
						showInfo(geocodingResponse);
					}
					
				} else {
					geocodingResponse.setOk(false);
					geocodingResponse.setStatusMsg(value);
				}
		    } else {
				//Es un Document vacio el que se ha obtenido de geocoding google
				geocodingResponse.setOk(false);
				geocodingResponse.setStatusMsg("RESPONSE EMPTY");
			}
		} catch (XPathExpressionException e) {
			LOG.error("GeocodingGoogleResponseParser error: " + e.toString());
		}
		
		return geocodingResponse;
	}
	
	/**
	 * showInfo.
	 * @param geocodingResponse GoogleGeocodingResponse
	 */
	public void showInfo(GoogleGeocodingResponse geocodingResponse) {
		
		if (!geocodingResponse.getGeocodingList().isEmpty()) {
			GeocodingDto dto = (GeocodingDto) geocodingResponse.getGeocodingList().get(0);
			LOG.debug("RESPONSE: OK:" + geocodingResponse.isOk() + " Tipo:" + dto.getType() + " Calle:" + dto.getStreet() 
					+ " Numero:" + dto.getNumber() + " Ciudad:" + dto.getCity() + " Region:" + dto.getRegion()
					+ " Pais:" + dto.getCountry() + " CP:" + dto.getZipCode() + " LAT:" + dto.getLat() + " LNG:"
					+ dto.getLng() + " QUALITY:" + dto.getQualitLatlng());
		}
	}
	
	/**
	 * getValue.
	 * Mediante el routeXpath obtenemos el valor y lo almacenamos en geocodingDto o geocodingResponse segun corresponda
	 * @param geocodingDto GeocodingDto
	 * @param geocodingResponse GoogleGeocodingResponse
	 * @param path String
	 * @param routeXpath String
	 * @param xpath XPath
	 */
	private void getValue(GeocodingDto geocodingDto, GoogleGeocodingResponse geocodingResponse, String path, String routeXpath, XPath xpath) {
		NodeList result = null;
		
		try {
			result = (NodeList) xpath.evaluate(path + routeXpath , geocoderResultDocument, XPathConstants.NODESET);
			
		    if (valid(result)) {
		    	String value = result.item(0).getNodeValue();
		    	
		    	if (routeXpath.equals(TYPE_XPATH)) {	
		    		geocodingDto.setType(value);
		    	} else if (routeXpath.equals(STREET_XPATH)) {	
		    		geocodingDto.setStreet(value);
				} else if (routeXpath.equals(STREET_NUMBER_XPATH)) {	
		    		geocodingDto.setNumber(value);
		    	} else if (routeXpath.equals(LOCALITY_XPATH)) {	
		    		geocodingDto.setCity(value);
				} else if (routeXpath.equals(CP_XPATH)) {
		    		geocodingDto.setZipCode(value);
		    	} else if (routeXpath.equals(PROVINCIE_XPATH)) {
		    		geocodingDto.setProvince(value);
				} else if (routeXpath.equals(MUNICIPALITY_XPATH)) {
		    		geocodingDto.setMunicipality(value);
				} else if (routeXpath.equals(STATE_XPATH)) {	
		    		geocodingDto.setRegion(value);
				} else if (routeXpath.equals(COUNTRY_XPATH)) {
		    		geocodingDto.setCountry(value);
				} else if (routeXpath.equals(LAT_XPATH)) {
		    		geocodingDto.setLat(value);
				} else if (routeXpath.equals(LNG_XPATH)) {
		    		geocodingDto.setLng(value);
				} else if (routeXpath.equals(QUALITY_LATLNG_XPATH)) {
		    		geocodingDto.setQualitLatlng(value);
				} else if (routeXpath.equals(VIEWPORT_SOUTHWEST_LAT_XPATH)) {
					geocodingResponse.setViewportSouthwestLat(Double.parseDouble(value));
				} else if (routeXpath.equals(VIEWPORT_SOUTHWEST_LNG_XPATH)) {
					geocodingResponse.setViewportSouthwestLng(Double.parseDouble(value));
				} else if (routeXpath.equals(VIEWPORT_NORTHEAST_LAT_XPATH)) {
					geocodingResponse.setViewportNortheastLat(Double.parseDouble(value));
				} else if (routeXpath.equals(VIEWPORT_NORTHEAST_LNG_XPATH)) {
					geocodingResponse.setViewportNortheastLng(Double.parseDouble(value));
				} else if (routeXpath.equals(BOUNDS_SOUTHWEST_LAT_XPATH)) {
					geocodingResponse.setBoundsSouthwestLat(Double.parseDouble(value));
				} else if (routeXpath.equals(BOUNDS_SOUTHWEST_LNG_XPATH)) {
					geocodingResponse.setBoundsSouthwestLng(Double.parseDouble(value));
				} else if (routeXpath.equals(BOUNDS_NORTHEAST_LAT_XPATH)) {
					geocodingResponse.setBoundsNortheastLat(Double.parseDouble(value));
				} else if (routeXpath.equals(BOUNDS_NORTHEAST_LNG_XPATH)) {
					geocodingResponse.setBoundsNortheastLng(Double.parseDouble(value));
				}
			}
		} catch (XPathExpressionException e) {
			LOG.error("GeocodingGoogleResponseParser error: " + e.toString());
		}
	}
	
	/**
	 * valid.
	 * Comprueba si nodeList tiene algun contenido	
	 * @param nodeList NodeList
	 * @return boolean
	 */
	private boolean valid(NodeList nodeList) {
		boolean res = false;
	
		if ((nodeList != null) && (nodeList.getLength() > 0)) {
			res = true;
		} else {
			res = false;
		}
		return res;
	}
	
}
