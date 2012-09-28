package com.deimos.perk.utils.geocoding;

import java.util.ArrayList;
import java.util.List;

import com.deimos.perk.utils.ParametersGoogle;
import com.deimos.perk.utils.ServiceParameters;



/**
 *
 * Factory pattern.
 *
 */
public final class GeocodingProviderFactory {

	/**
	 * Use for select geocodingProvider implementation.
	 *
	 */
	public enum GeocodingProviderType {
		GOOGLE("google"),
		CERCALIA("cercalia");
		
		private String id;
		private GeocodingProviderType(String id) {
			this.id = id;
		}
		
		public String getId() {
			return id;
		}
		
		public static GeocodingProviderType parse(String type) {
			GeocodingProviderType salida = GOOGLE;
			for (GeocodingProviderType item : GeocodingProviderType.values()) {
				if (item.getId().equals(type)) {
					salida = item;
					break;
				}
			}
			return salida;
		}
	}
	

	/**
	 * Constructor.
	 */
	private GeocodingProviderFactory() {
		super();
	}

	/**
	 * @param type GeocodingProvider
	 * @return getGeocodingProvider
	 * @throws Exception exception
	 */
	public static synchronized GeocodingProvider getGeocodingProvider(GeocodingProviderType type, 
			ServiceParameters geoParameters) throws Exception {
		GeocodingProvider geocodingProvider = null;
		switch (type) {
		case GOOGLE:
			geocodingProvider = GeocodingGoogle.getInstance(geoParameters);
			break;
		default:
			break;
		}
		return geocodingProvider;
	}

	public static void main(String[] argv) {
		//Profiler
//		# Define la url del proveedor de cartografia -CERCALIA - Laboratorio y en desarrollo
//		 urlCercalia=http://c1.cercalia.com
//		 urlGeocodingCercalia=/clients/DEIMOS?
//
//		# Define la url del proveedor de cartografia -Google - 
//		 urlGoogle=http\://maps.google.com 
//		 urlGeocodingGoogle=/maps/api/geocode/xml?
//
//
//		# Clave de google maps por direccion. 
//		# Client id de GM API Premier
//		 googleMapsClientId=gme-deimosspaceslu1
//
//		# Geocoding provider
//		 geocodingProvider=Google
//
//		 timeBetweenGeoRequests=200
//		 timeBetweenGeoErrors=2000
//		 overQueryLimit=OVER_QUERY_LIMIT
//		 googleKey=RltyFple2LsNygUrh3akpGyQ-OE=

		try {
			GeocodingProviderType geocodingProviderType = GeocodingProviderType.GOOGLE;
			ServiceParameters geocodingParameters = new ParametersGoogle(
						"/maps/api/geocode/xml?",
						"http://maps.google.com",
						"gme-deimosspaceslu1",
						"OVER_QUERY_LIMIT",
						"RltyFple2LsNygUrh3akpGyQ-OE=");

			GeocodingProvider geocodingProvider = GeocodingProviderFactory.getGeocodingProvider(geocodingProviderType, geocodingParameters);


			List<GeocodingDto> geocodingDtoList = new ArrayList<GeocodingDto>();	

			//-4.721205,41.516170
			
			double latitude = 40.5;
			double longitude = -3.5;

			for (Integer i = 0; i < 50; i++) {
				GeocodingDto geocodingDto = new GeocodingDto();
				geocodingDto.setId(i + 1);
				geocodingDto.setLat(String.valueOf(latitude));
				geocodingDto.setLng(String.valueOf(longitude));
				latitude = latitude + 0.1;
				longitude = longitude - 0.1;

				geocodingDtoList.add(geocodingDto);
			}

			List<GeocodingDto> listgeocodingDto = geocodingProvider.getGeocoding(geocodingDtoList);
			for (GeocodingDto geocodingDto : listgeocodingDto) {
				System.out.println("Lugar: " + geocodingDto.getLocation());
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
