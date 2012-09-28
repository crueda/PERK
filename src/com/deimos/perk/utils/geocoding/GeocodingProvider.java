package com.deimos.perk.utils.geocoding;

import java.util.List;


/**
 * GeocodingProvider.
 * 
 * @author rojl
 *
 */
public abstract class GeocodingProvider {

	public GeocodingProvider() { }

	public abstract List<GeocodingDto> getGeocoding(List<GeocodingDto> list);
	public abstract List<GeocodingDto> getReverseGeocoding(List<GeocodingDto> geocodingDto);

}
