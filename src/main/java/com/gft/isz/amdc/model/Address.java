package com.gft.isz.amdc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

	@JsonProperty("number")
	private String number;

	@JsonProperty("postCode")
	private String postCode;

	public Address() {
	}
	
	public Address(String number, String postCode) {
		this.number = number;
		this.postCode = postCode;
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
		} else {
			ret = false;
		}
		
		return ret;
	}
}
