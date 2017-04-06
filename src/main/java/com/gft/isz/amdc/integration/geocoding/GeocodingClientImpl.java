package com.gft.isz.amdc.integration.geocoding;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gft.isz.amdc.model.Location;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

@Component
public class GeocodingClientImpl implements GeocodingClient {
	
	private static final GeoApiContext GA_CONTEXT = new GeoApiContext().setApiKey("AIzaSyBERfoiR0nPdyszo9Hc6pbmh7aIskn0z_g");
	
	@Override
	public Location getLocation(String postCode) throws ApiException, InterruptedException, IOException {
		/* This could be done asynchronously. */
		GeocodingResult[] results =  GeocodingApi.geocode(GA_CONTEXT, postCode).await();
		LatLng location = results[0].geometry.location;
		return new Location(location.lat, location.lng);
	}

}
