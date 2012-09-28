package com.deimos.perk.utils.geocoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.deimos.perk.utils.ParametersGoogle;
import com.deimos.perk.utils.ServiceParameters;
import com.deimos.perk.utils.UrlSigner;


public class GeocodingGoogle extends GeocodingProvider {
	
	private static final String ENCODING = "UTF-8";
	private static final String ZERO_RESULTS = "ZERO_RESULTS";
	private static final String NO_GEO_INFO = "...";
	
	private static GeocodingGoogle instanceGeocodingGoogle = null;
	
	/**
	 * getInstance.
	 * 
	 * @return DataSourceVehicleEventManagement
	 * @throws Exception exception
	 */
	public static synchronized GeocodingGoogle getInstance(ServiceParameters geocodingParametersGoogle) throws Exception {
		if (instanceGeocodingGoogle == null) {
			instanceGeocodingGoogle = new GeocodingGoogle((ParametersGoogle) geocodingParametersGoogle);
		}
		return instanceGeocodingGoogle;
	}
	
	//Instancia
	private final Logger log = Logger.getLogger(GeocodingGoogle.class.getName());

	private boolean sensor = false;
	private String language = "es";
	
	private ParametersGoogle geocodingParameters = null;
	
	/**
	 * GeocodingGoogle.
	 */
	public GeocodingGoogle(ParametersGoogle geocodingParameters) {
		super();
		setGeocodingParameters(geocodingParameters);
	}

	public ParametersGoogle getGeocodingParameters() {
		return geocodingParameters;
	}

	public void setGeocodingParameters(ParametersGoogle geocodingParameters) {
		this.geocodingParameters = geocodingParameters;
	}

	/**
	 * This method return the List of geocoding info.
	 * 
	 * @param geocodingDtoList Lista de GeocodingDto
	 * @return GeocodingDto
	 */
	public List<GeocodingDto> getGeocoding(List<GeocodingDto> geocodingDtoList) {
		List<GeocodingDto> salida = new ArrayList<GeocodingDto>();
		

		for (GeocodingDto geocodingDto : geocodingDtoList) {
			if (geocodingDto != null) {
				String lat = geocodingDto.getLat();
				String lng = geocodingDto.getLng();
				
				try {
					//construimos la url de peticion de geocoding
					StringBuffer urlParameters = new StringBuffer();
					urlParameters.append("latlng=");
					urlParameters.append(lat + "," + lng);
					urlParameters.append("&language=");
					urlParameters.append(this.language);
					urlParameters.append("&sensor=");
					urlParameters.append(this.sensor);
					urlParameters.append("&client=");
					urlParameters.append(getGeocodingParameters().getClientId());
					
					GeocodingDto bestGeocoding = getGeocoding(urlParameters, geocodingDto.getId());
					if (bestGeocoding != null) {
						salida.add(bestGeocoding);
					}
				} catch (Exception e) {
					log.error("GeocodingGoogle error: " + e.toString());
				}
			}
		}
		return salida;
	}
	
	/**
	 * getGeocoding.
	 * 
	 * @param urlParameters StringBuffer
	 * @param geocodingId Integer
	 * @return GeocodingDto 
	 * @throws Exception
	 */
	private GeocodingDto getGeocoding(StringBuffer urlParameters, Integer geocodingId) throws Exception {
		GeocodingDto salida = null;
		try {
			Document geocoderResultDocument = getDocumentGeocodingResponse(urlParameters.toString());
			salida = getGeocoding(geocodingId, geocoderResultDocument);
		} catch (Exception error) {
			log.error(error);
		}
		return salida;
	}

	/**
	 * getGeocoding.
	 * 
	 * @param geocodingId
	 * @param salida
	 * @param geocoderResultDocument
	 * @return
	 */
	public GeocodingDto getGeocoding(Integer geocodingId, Document geocoderResultDocument) {
		GeocodingDto salida = null;
		try {
			if (geocoderResultDocument != null) {
				
				GoogleGeocodingResponseParser geocodingParser = new GoogleGeocodingResponseParser(geocoderResultDocument);
				
				GoogleGeocodingResponse googleGeocodingResponse = new GoogleGeocodingResponse();
				geocodingParser.parser(googleGeocodingResponse);
				
				if (googleGeocodingResponse.isOk()) { //Si es ok es que al menos hay un resultado
					GeocodingDto bestGeocoding = googleGeocodingResponse.getBestGeocodingDto();
					if (bestGeocoding != null) {
						bestGeocoding.setId(geocodingId);
						salida = bestGeocoding;
					}
					log.debug("Geocoding obtained from Google:");
				} else {
					String statusMsg = googleGeocodingResponse.getStatusMsg();
					log.error("Google Geocoding error: " + statusMsg);
					
					// Si no hay resultados se fija como localizacion no disponible para no volverse a pedir
					if (ZERO_RESULTS.equals(statusMsg)) {
						GeocodingDto errorGeocoding = new GeocodingDto();
						errorGeocoding.setId(geocodingId);
						errorGeocoding.setLocation(NO_GEO_INFO);
						salida = errorGeocoding;
					}
				}
			}
		} catch (Exception error) {
			log.error(error);
		}
		return salida;
	}
	
	/**
	 * This method return the List of reverse geocoding info.
	 * 
	 * @param geocodingDtoList list of geocodingDto
	 * @return geocodingDtoList list of geocodingDto
	 */
	public List<GeocodingDto> getReverseGeocoding(List<GeocodingDto> geocodingDtoList) {
		
		List<GeocodingDto> salida = new ArrayList<GeocodingDto>();

		for (GeocodingDto current : geocodingDtoList) {
			//String road, String number, String locality, String province, String cp, String region, String country
			String road = current.getStreet();
			String number = current.getNumber();
			String locality = current.getCity();
			String province = current.getProvince();
			String cp = "";
			String region = "";
			String country = "";

			try {
				//construimos la url de peticion de geocoding
				StringBuffer urlParameterStringBuffer = new StringBuffer();   
				urlParameterStringBuffer.append("address=");
				if (!road.isEmpty()) {
					road = parseString(road);
					urlParameterStringBuffer.append(road.trim());
				}
				if (!number.isEmpty()) {
					urlParameterStringBuffer.append("+");
					number = parseString(number); 
					urlParameterStringBuffer.append(number.trim());
				}
				if (!locality.isEmpty()) {
					urlParameterStringBuffer.append("+");
					locality = parseString(locality);
					urlParameterStringBuffer.append(locality.trim());
				}
				if (!province.isEmpty()) {
					urlParameterStringBuffer.append("+");
					province = parseString(province);
					urlParameterStringBuffer.append(province.trim());
				}
				if (!cp.isEmpty()) {
					urlParameterStringBuffer.append("+");
					cp = parseString(cp);
					urlParameterStringBuffer.append(cp.trim());
				}
				if (!region.isEmpty()) {
					urlParameterStringBuffer.append("+");
					region = parseString(region);
					urlParameterStringBuffer.append(region.trim());
				}
				if (!country.isEmpty()) {
					urlParameterStringBuffer.append("+");
					country = parseString(country); 
					urlParameterStringBuffer.append(country);
				}
				urlParameterStringBuffer.append("&language=");
				urlParameterStringBuffer.append(this.language);
				urlParameterStringBuffer.append("&sensor=");
				urlParameterStringBuffer.append(this.sensor);
				urlParameterStringBuffer.append("&client=");
				urlParameterStringBuffer.append(getGeocodingParameters().getClientId());

				String urlParameterString = urlParameterStringBuffer.toString();
				urlParameterString = urlParameterString.replaceAll(" ", "+");

				GeocodingDto bestGeocoding  = getReverseGeocoding(getDocumentGeocodingResponse(urlParameterString));
				if (bestGeocoding != null) {
					if (current.getId() != null) {
						bestGeocoding.setId(current.getId());
					}
					
					if (!number.isEmpty() && (bestGeocoding.getNumber() == null || bestGeocoding.getNumber().equals(""))) {
						bestGeocoding.setNumber(current.getNumber());
					} 
					salida.add(bestGeocoding);
				}
			} catch (Exception e) {
				log.error("ReverseGeocodingGoogle error: " + e.toString());
			}
		}
		return salida;		
	}

	/**
	 * getReverseGeocoding.
	 * 
	 * @param geocoderResultDocument Document
	 * @return GeocodingDto
	 * @throws Exception
	 */
	public GeocodingDto getReverseGeocoding(Document geocoderResultDocument) throws Exception {
		GeocodingDto salida = null;
		try {
			if (geocoderResultDocument != null) {
				GoogleGeocodingResponse googleGeocodingResponse = new GoogleGeocodingResponse();
				GoogleGeocodingResponseParser geocodingParser = new GoogleGeocodingResponseParser(geocoderResultDocument);
				geocodingParser.parser(googleGeocodingResponse);
				
				if (googleGeocodingResponse.isOk()) {
					//TYPE_ROUTE indica que es una carretera. Si no hay de este tipo, obtenemos el primero
					GeocodingDto bestGeocoding = googleGeocodingResponse.getGeocodingDto(GoogleGeocodingResponse.TYPE_ROUTE);
					if (bestGeocoding == null) {
						if (!googleGeocodingResponse.getGeocodingList().isEmpty()) {
							bestGeocoding = (GeocodingDto) googleGeocodingResponse.getGeocodingList().get(0);
						}
					}
					salida = bestGeocoding;
					log.debug("Reverse Geocoding obtained from Google:");
				} else {
					String statusMsg = googleGeocodingResponse.getStatusMsg();

					log.error("Google Reverse Geocoding error: " + statusMsg);
					
					// Si no hay resultados se fija como localizacion no disponible para no volverse a pedir
					if (ZERO_RESULTS.equals(statusMsg)) {
						GeocodingDto errorGeocoding = new GeocodingDto();
						errorGeocoding.setLocation(NO_GEO_INFO);
						salida = errorGeocoding;
					}
				}
			}
		} catch (Exception error) {
			log.error(error);
		}
		return salida;
	}
	
	/**
	 * Establece comunicacion y obtiene la respuesta del servicio de geocoding de google.
	 * @param urlParameters url de la peticion de geocoding
	 * @return el Document devuelto por google
	 * @throws Exception excepcion
	 */
	private Document getDocumentGeocodingResponse(String urlParameters) throws Exception {
		Document salida = null;
		try {
			// leemos el resultado y parseamos a un documento XML
			salida = getDocument(getInputStreamGeocodingResponse(urlParameters));
		} catch (SAXException e) {
			throw new Exception("Error al parsear a documento XML" + ".\n The error message is:" + e);		
		} catch (ParserConfigurationException e) {
			throw new Exception("Error al parsear a documento XML" + ".\n The error message is:" + e);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return salida;
	}
	
	/**
	 * Establece comunicacion y obtiene la respuesta del servicio de geocoding de google.
	 * 
	 * @param urlParameters url de la peticion de geocoding
	 * @return el Document devuelto por google
	 * @throws Exception excepcion
	 */
	public Document getDocument(String data) throws Exception {
		Document salida = null;
		try {
			// Abre la conexion y obtenemos resultados como InputSource
			InputSource geocoderResultInputSource = new InputSource(new StringReader(data));
			
			// leemos el resultado y parseamos a un documento XML
			salida = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(geocoderResultInputSource);

		} catch (SAXException e) {
			throw new Exception("Error al parsear a documento XML" + ".\n The error message is:" + e);		
		} catch (ParserConfigurationException e) {
			throw new Exception("Error al parsear a documento XML" + ".\n The error message is:" + e);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return salida;
	}
	
	/**
	 * getInputStreamGeocodingResponse
	 * Establece comunicacion y obtiene la respuesta del servicio de geocoding de google.
	 * 
	 * @param urlString url de la peticion de geocoding
	 * @return InputStream
	 * @throws Exception excepcion
	 */
	public String getInputStreamGeocodingResponse(String urlParameters) throws Exception {
		String salida = null;
		HttpURLConnection conn = null;
		URL url = null;
		try {
			
			UrlSigner urlSigner = new UrlSigner(getGeocodingParameters().getGoogleKey());
			String signedUrl = urlSigner.signRequest(getGeocodingParameters().getUrlService(), urlParameters);
			
			if (getGeocodingParameters().getUrlProvider() != null && !getGeocodingParameters().getUrlProvider().equals("")) {
				url = new URL(getGeocodingParameters().getUrlProvider() + signedUrl);
				
				conn = (HttpURLConnection) url.openConnection();
				
				// Devolvemos un inputStream en memoria
				BufferedReader origen = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODING));
	
				String line = null;
				StringBuffer stringBuffer = new StringBuffer();
				while ((line = origen.readLine()) != null) {
					stringBuffer.append(line);
				}

				salida = new String(stringBuffer.toString().getBytes());
			} else {
				log.info("Geocoding provider not defined: Geocoding disabled");
			}
		} catch (MalformedURLException e) {
			throw new Exception("Malformed URL Error: " + url.toString() + ".\n The error message is:" + e);
		} catch (IOException e) {
			throw new Exception("IOException: " + url.toString() + ".\n The error message is:" + e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return salida;
	}
	
	/**
	 * parseString.
	 * Sustituye caracteres que dan error al solicitar geocoding a google, como Ã‡,Ã±,Ã¡,Ã©,Ã­,Ã³,Ãº,etc...
	 * 
	 * @param cad cadena de texto en la que sustituir los caracteres prohibidos
	 * @return cadena de texto sin caracteres prohibidos
	 */
	private String parseString(String cad) {
		String[][] forbiden = {{"Ã§", "s"}, {"Ã‡", "S"}, {"Ã±", "n"}, {"Ã‘", "N"}, {"Ã¡", "a"}, {"Ã�", "A"}, {"Ã©", "e"},
				   {"Ã‰", "E"}, {"Ã­", "i"}, {"Ã�", "I"}, {"Ã³", "o"}, {"Ã“", "O"}, {"Ãº", "u"}, {"Ãš", "U"}};
		
		String res = cad;
		if (res != null) {
			for (int i = 0; i < forbiden.length; i++) {
				res = res.replaceAll(forbiden[i][0], forbiden[i][1]);
			}
		}
		return res;
	}
	
}
