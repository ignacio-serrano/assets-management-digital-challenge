package com.gft.isz.amdc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
	@JsonProperty
	public final double latitude;
	
	@JsonProperty
	public final double longitude;

	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
