package com.deimos.perk.utils.geocoding;

/**
 * GeocodingDto.
 * 
 *
 */
public class GeocodingDto extends DtoObject implements Comparable<GeocodingDto> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6638344120467864868L;
	
	
	private Integer id;
	private String lat;
	private String lng;

	private String type;
	private String street;
	private String ine;
	private String city;
	private String municipality;
	private String province;
	private String region;
	private String country;
	private String number;
	private String zipCode;
	private String location;

	private String qualitLatlng; 

	/**
	 * Constructor.
	 * 
	 */
	public GeocodingDto() { 
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param ident Integer
	 * @param latitude String
	 * @param longitude String
	 */
	public GeocodingDto(final Integer ident, final String latitude, final String longitude) {
		super();
		this.id = ident;
		this.lat = latitude;
		this.lng = longitude;
	}

	/**
	 * Constructor.
	 * 
	 * @param ident Integer
	 * @param streetLoc String
	 * @param numberLoc String
	 * @param cityLoc String
	 * @param municipalityLoc String
	 * @param zipCodeLoc String
	 * @param regionLoc String
	 * @param countryLoc String
	 */
	public GeocodingDto(final Integer ident, final String streetLoc, final String numberLoc, final String cityLoc, 
			final String municipalityLoc, final String zipCodeLoc, final String regionLoc, final String countryLoc) {
		super();
		this.id 			= ident;
		this.street 		= streetLoc;
		this.city 			= cityLoc;
		this.municipality	= municipalityLoc;
		this.region 		= regionLoc;
		this.number			= numberLoc;
		this.zipCode		= zipCodeLoc;
		this.country		= countryLoc;
	}

	/**
	 * Constructor.
	 * 
	 * @param ident Integer
	 * @param typeLoc String
	 * @param streetLoc String
	 * @param numberLoc String
	 * @param cityLoc String
	 * @param municipalityLoc String
	 * @param zipCodeLoc String
	 * @param regionLoc String
	 * @param countryLoc String
	 * @param latitude String
	 * @param longitude String
	 * @param quality String
	 */
	public GeocodingDto(final Integer ident, final String typeLoc, final String streetLoc, final String numberLoc, final String cityLoc, 
			final String municipalityLoc, final String zipCodeLoc, final String regionLoc, final String countryLoc, final String latitude, 
			final String longitude, final String quality) {
		super();
		this.id 			= ident;
		this.type			= typeLoc;
		this.street 		= streetLoc;
		this.city 			= cityLoc;
		this.municipality	= municipalityLoc;
		this.region 		= regionLoc;
		this.number			= numberLoc;
		this.zipCode		= zipCodeLoc;
		this.country		= countryLoc;
		this.lat			= latitude;
		this.lng			= longitude;
		this.qualitLatlng = quality;
	}

	/**
	 * getId.
	 * 
	 * @return Integer
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * setId.
	 * 
	 * @param ident Integer
	 */
	public void setId(final Integer ident) {
		this.id = ident;
	}

	/**
	 * getType.
	 * 	
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * setType.
	 * 
	 * @param typeLoc String
	 */
	public void setType(final String typeLoc) {
		this.type = typeLoc;
	}

	/**
	 * getStreet.
	 * 
	 * @return String
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * setStreet.
	 * 
	 * @param streetLoc String
	 */
	public void setStreet(final String streetLoc) {
		this.street = streetLoc;
	}

	/**
	 * getIne.
	 * 
	 * @return String
	 */
	public String getIne() {
		return ine;
	}

	/**
	 * setIne.
	 * 
	 * @param ineCode String
	 */
	public void setIne(final String ineCode) {
		this.ine = ineCode;
	}
	
	/**
	 * getCity.
	 * 
	 * @return String
	 */
	public String getCity() {
		return city;
	}

	/**
	 * setCity.
	 * 
	 * @param cityLoc String
	 */
	public void setCity(final String cityLoc) {
		this.city = cityLoc;
	}
	
	/**
	 * getMunicipality.
	 * 
	 * @return String
	 */
	public String getMunicipality() {
		return municipality;
	}

	/**
	 * setMunicipality.
	 * 
	 * @param municipalityLoc String
	 */
	public void setMunicipality(final String municipalityLoc) {
		this.municipality = municipalityLoc;
	}

	/**
	 * getRegion.
	 * 
	 * @return String
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * setRegion.
	 * 
	 * @param regionLoc String
	 */
	public void setRegion(final String regionLoc) {
		this.region = regionLoc;
	}

	/**
	 * getNumber.
	 * 
	 * @return String
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * setNumber.
	 * 
	 * @param numberLoc String
	 */
	public void setNumber(final String numberLoc) {
		this.number = numberLoc;
	}

	/**
	 * getCountry.
	 * 
	 * @return String
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * setCountry.
	 * 
	 * @param countryLoc String
	 */
	public void setCountry(final String countryLoc) {
		this.country = countryLoc;
	}

	/**
	 * getZipCode.
	 * 
	 * @return String
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * setZipCode.
	 * 
	 * @param zipCodeLoc String
	 */
	public void setZipCode(final String zipCodeLoc) {
		this.zipCode = zipCodeLoc;
	}

	/**
	 * getLat.
	 * 
	 * @return String
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * setLat.
	 * 
	 * @param latitude String
	 */
	public void setLat(final String latitude) {
		this.lat = latitude;
	}

	/**
	 * getLng.
	 * 
	 * @return String
	 */
	public String getLng() {
		return lng;
	}

	/**
	 * setLng.
	 * 
	 * @param longitude String
	 */
	public void setLng(final String longitude) {
		this.lng = longitude;
	}

	/**
	 * getQualitLatlng.
	 * 
	 * @return String
	 */
	public String getQualitLatlng() {
		return qualitLatlng;
	}

	/**
	 * setQualitLatlng.
	 * 
	 * @param quality String
	 */
	public void setQualitLatlng(final String quality) {
		qualitLatlng = quality;
	}

	/**
	 * getProvince.
	 * 
	 * @return String
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * setProvince.
	 * 
	 * @param provinceLoc String
	 */
	public void setProvince(final String provinceLoc) {
		this.province = provinceLoc;
	}

	/**
	 * getLocation.
	 * 
	 * @return location parsed as String
	 */
	public String getLocation() {
		if (location == null) {

			final String  streetLoc 	= getStreet();		                	
			final String  numberLoc 	= getNumber();
			final String  zipCodeLoc	= getZipCode();
			final String  cityLoc		= getCity();
			final String  regionLoc 	= getRegion();
			final String  provinceLoc	= getProvince();
			final String  countryLoc	= getCountry();

			if (streetLoc != null) {
				location = streetLoc;
			}

			if (numberLoc != null) {
				location = location + ", " + numberLoc; 	
			}

			if (zipCodeLoc != null) {
				location = location + ", " + zipCodeLoc;
			}

			if (cityLoc != null) {
				location = location + ", " + cityLoc;
			}

			if (provinceLoc != null) {
				location = location + ", " + provinceLoc;
			}

			if (regionLoc != null) {
				location = location + ", (" + regionLoc + ")";
			}

			if (countryLoc != null) {
				location = location + ", " + countryLoc;
			}
		}

		return location;

	}

	/**
	 * setLocation.
	 * 
	 * @param parsedLoc String
	 */
	public void setLocation(final String parsedLoc) {
		this.location = parsedLoc;
	}

	/**
	 * equals.
	 * 
	 * @param ident String
	 * @return true if equals false otherwise
	 */
	public boolean equals(final String ident) {
		boolean salida = false;
		if (ident != null && this.id != null &&	ident.equals(this.id.toString())) {
			salida = true;
		}
		return salida;
	}

	/**
	 * equals.
	 * 
	 * @param obj Object
	 * @return true if equals false otherwise
	 */
	public boolean equals(final Object obj) {
		boolean salida = true;
		
		if (this == obj) {
			salida = true;
		} else {
			if (!(obj instanceof GeocodingDto)) {
				salida = false;
			} else {
				final GeocodingDto that = (GeocodingDto) obj;
				if (!(that.id == null ? this.id == null : that.id.equals(this.id))) {
					salida = false;
				} else {
					if (!(that.street == null ? this.street == null : that.street.equals(this.street))) {     
						salida = false;
					} else {
						if (!(that.city == null ? this.city == null : that.city.equals(this.city))) {
							salida = false;
						} else {
							if (!(that.municipality == null ? this.municipality == null : that.municipality.equals(this.municipality))) {     
								salida = false;		
							} else {
								if (!(that.region == null ? this.region == null : that.region.equals(this.region))) {     
									salida = false;
								} else {
									if (!(that.number == null ? this.number == null : that.number.equals(this.number))) {     
										salida = false;
									} else {
										if (!(that.zipCode == null ? this.zipCode == null : that.zipCode.equals(this.zipCode))) {     
											salida = false;
										}
									}
								}
							}
						}
					}
				}
			}
		}
			
		return salida;
	}

	/**
	 * hashCode.
	 * 
	 * @return int
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + this.id.hashCode();
		result = 37 * result + this.street.hashCode();
		result = 37 * result + this.city.hashCode();
		result = 37 * result + this.municipality.hashCode();		
		result = 37 * result + this.region.hashCode();
		result = 37 * result + this.number.hashCode();
		result = 37 * result + this.zipCode.hashCode();
		return result;
	}

	/**
	 * toString.
	 * 
	 * @return as String
	 */
	public String toString() {
		final StringBuffer returnString = new StringBuffer();
	       
		returnString.append(id);
		returnString.append(", ");
		returnString.append(street);
		returnString.append(", ");
		returnString.append(city);
		returnString.append(", ");
		returnString.append(municipality);
		returnString.append(", ");
		returnString.append(region);	
		returnString.append(", ");
		returnString.append(number);	
		returnString.append(", ");
		returnString.append(zipCode);	
									
		return returnString.toString();	
	}

	/**
	 * compareTo.
	 * 
	 * @param otro GeocodingDto
	 * @return int
	 */
	public int compareTo(final GeocodingDto otro) {
		return id.compareTo(otro.getId());
	}
}

