package com.gft.isz.amdc.integration.geocoding;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GeocodingClient {
	
	private static final String GOOGLE_GEOCODE_URL = "http://maps.googleapis.com/maps/api/geocode/json";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Location getLocation(String postCode) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GOOGLE_GEOCODE_URL).queryParam("address", postCode);
		
		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(builder.build().encode().toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
		Map body = response.getBody();
		
		List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
		Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
		Map<String, Double> location = (Map<String, Double>) geometry.get("location");
		
		return new Location(location.get("lat"), location.get("lng"));
	}

}
