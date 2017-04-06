package com.gft.isz.amdc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Shop {
	
	@JsonProperty("shopName")
	private String name;
	
	@JsonProperty("shopAddress")
	private Address address;
	
	@JsonIgnore
	private Double latitude;
	
	@JsonIgnore
	private Double longitude;

	public Shop() {}
	
	public Shop(String name, Address address) {
		this.name = name;
		this.address = address;
	}
	
	public Shop(String name, Address address, Double latitude, Double longitude) {
		this(name, address);
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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
		
		if (obj instanceof Shop) {
			Shop that = (Shop) obj;
			if (this.getName() != null) {
				ret = this.getName().equals(that.getName());
			} else {
				ret = that.getName() == null;
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
			
			if (ret) {
				if (this.getAddress() != null) {
					ret = this.getAddress().equals(that.getAddress());
				} else {
					ret = this.getAddress() == null;
				}
			}
		} else {
			ret = false;
		}
		
		return ret;
	}
}
