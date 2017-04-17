package com.gft.isz.amdc.model;

/* When possible, it's better to use immutable objects like this rather than
 * beans full of getters and setters. */
public class Location {
	public final double latitude;
	
	public final double longitude;

	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
