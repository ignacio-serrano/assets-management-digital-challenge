package com.gft.isz.amdc.integration.geocoding;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gft.isz.amdc.model.Location;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

/* There are many ways to interact with Google Maps. RestTemplate would have 
 * been my choice if I hadn't found an already tested client. Anyway, it's 
 * hidden behind this class just in case a better method appears (substitution
 * will be less intrusive). */
@Component
public class GeocodingClientImpl implements GeocodingClient {

	private static final GeoApiContext GA_CONTEXT = new GeoApiContext()
			.setApiKey("AIzaSyBERfoiR0nPdyszo9Hc6pbmh7aIskn0z_g");

	@Autowired
	private ExecutorService executorService;

	/* I wrote this method before making the call asynchronous. I chose to keep
	 * it to separate concerns and because in further developments it may be
	 * necessary to synchronously call Google Maps. */
	@Override
	public Location getLocation(String postCode) throws ApiException, InterruptedException, IOException {
		GeocodingResult[] results = GeocodingApi.geocode(GA_CONTEXT, postCode).await();
		LatLng location = results[0].geometry.location;
		return new Location(location.lat, location.lng);
	}

	/* This is an asynchronous version of the former method. It could be done
	 * the same using the "setCallback" method on Google Maps client but the
	 * wording said that you
	 * "pay a lot of attention to producing thread-safe code" and I thought this
	 * was the best chance in the challenge to demonstrate multi-threading
	 * knowledge. */
	@Override
	public void getLocationAsync(String postCode, Callback callback) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				Location location = null;
				try {
					location = getLocation(postCode);
				} catch (Exception e) {
					throw new RuntimeException("Error calling Google Maps service.", e);
				}
				callback.onSuccess(location);
			}
		});
	}

	@FunctionalInterface
	public static interface Callback {
		void onSuccess(Location location);
	}
}
