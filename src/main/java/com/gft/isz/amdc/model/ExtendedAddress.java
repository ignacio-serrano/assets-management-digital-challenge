package com.gft.isz.amdc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtendedAddress extends Address {
	
	@JsonProperty("location")
	private Location location;
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
}
