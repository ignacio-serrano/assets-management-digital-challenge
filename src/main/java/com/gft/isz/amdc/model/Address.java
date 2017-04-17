package com.gft.isz.amdc.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Cloneable {

	@NotEmpty
	@JsonProperty("number")
	private String number;

	@NotEmpty
	@JsonProperty("postCode")
	private String postCode;

	private Double latitude;
	
	private Double longitude;

	public Address() {
	}
	
	public Address(String number, String postCode) {
		this.number = number;
		this.postCode = postCode;
	}

	public Address(String number, String postCode, Double latitude, Double longitude) {
		this(number, postCode);
		this.latitude = latitude;
		this.longitude = longitude;
	}
		
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		boolean ret = true;
		
		if (obj instanceof Address) {
			Address that = (Address) obj;
			if (this.getNumber() != null) {
				ret = this.getNumber().equals(that.getNumber());
			} else {
				ret = that.getNumber() == null;
			}
			
			if (ret) {
				if (this.getPostCode() != null) {
					ret = this.getPostCode().equals(that.getPostCode());
				} else {
					ret = that.getPostCode() == null;
				}
			}
			if (ret) {
				if (this.getLatitude() != null) {
					ret = this.getLatitude().equals(that.getLatitude());
				} else {
					ret = that.getLatitude() == null;
				}
			}
			
			if (ret) {
				if (this.getLongitude() != null) {
					ret = this.getLongitude().equals(that.getLongitude());
				} else {
					ret = that.getLongitude() == null;
				}
			}
			
		} else {
			ret = false;
		}
		
		return ret;
	}
	
	@Override
	public Object clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			/* This can't actually happen unless someone changes base class. If that's the case,
			 * better warn the programmer. */
			throw new RuntimeException("Did you forget to override clone() in base class?", e);
		}
		return clone;
	}
}
