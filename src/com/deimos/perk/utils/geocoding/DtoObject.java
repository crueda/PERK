package com.deimos.perk.utils.geocoding;

import java.io.Serializable;

/**
 * DtoObject.
 *
 */
public class DtoObject implements Serializable, Introspectable {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public DtoObject() {
		super();
	}
	
	/**
	 * equals.
	 * 
	 * @param idDtoObject String
	 * @return boolean
	 * */
	public boolean equals(final String idDtoObject) {
		return false;
	}
	
	/**
	 * getDtoId.
	 * 
	 * @return String
	 * */
	public String getDtoId() {
		return "";
	}
	
	/**
	 *  getDtoDescription.
	 *  
	 *  @return String
	 * */
	public String getDtoDescription() {
		return "";
	}
	
	/**
     * hashCode.
     * 
     * @return int
     */
    public int hashCode() {
        int result = 17;
        
        return result;
    }

}

