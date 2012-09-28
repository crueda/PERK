package com.deimos.perk.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="in0" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in1" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in2" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="in3" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="in4" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="in5" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="in6" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "in0", "in1", "in2", "in3", "in4", "in5",
		"in6" })
@XmlRootElement(name = "addTagTracking")
public class AddTagTracking {

	@XmlElement(required = true, nillable = true)
	protected String in0;
	@XmlElement(required = true, type = Long.class, nillable = true)
	protected Long in1;
	@XmlElement(required = true, type = Double.class, nillable = true)
	protected Double in2;
	@XmlElement(required = true, type = Double.class, nillable = true)
	protected Double in3;
	@XmlElement(required = true, type = Integer.class, nillable = true)
	protected Integer in4;
	@XmlElement(required = true, type = Integer.class, nillable = true)
	protected Integer in5;
	@XmlElement(required = true, nillable = true)
	protected String in6;

	/**
	 * Gets the value of the in0 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIn0() {
		return in0;
	}

	/**
	 * Sets the value of the in0 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIn0(String value) {
		this.in0 = value;
	}

	/**
	 * Gets the value of the in1 property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getIn1() {
		return in1;
	}

	/**
	 * Sets the value of the in1 property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setIn1(Long value) {
		this.in1 = value;
	}

	/**
	 * Gets the value of the in2 property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getIn2() {
		return in2;
	}

	/**
	 * Sets the value of the in2 property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setIn2(Double value) {
		this.in2 = value;
	}

	/**
	 * Gets the value of the in3 property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getIn3() {
		return in3;
	}

	/**
	 * Sets the value of the in3 property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setIn3(Double value) {
		this.in3 = value;
	}

	/**
	 * Gets the value of the in4 property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getIn4() {
		return in4;
	}

	/**
	 * Sets the value of the in4 property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setIn4(Integer value) {
		this.in4 = value;
	}

	/**
	 * Gets the value of the in5 property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getIn5() {
		return in5;
	}

	/**
	 * Sets the value of the in5 property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setIn5(Integer value) {
		this.in5 = value;
	}

	/**
	 * Gets the value of the in6 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIn6() {
		return in6;
	}

	/**
	 * Sets the value of the in6 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIn6(String value) {
		this.in6 = value;
	}

}
