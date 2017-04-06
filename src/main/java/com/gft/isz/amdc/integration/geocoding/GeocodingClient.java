package com.gft.isz.amdc.integration.geocoding;

import java.io.IOException;

import com.gft.isz.amdc.model.Location;
import com.google.maps.errors.ApiException;

public interface GeocodingClient {
	public Location getLocation(String postCode) throws ApiException, InterruptedException, IOException;
}
