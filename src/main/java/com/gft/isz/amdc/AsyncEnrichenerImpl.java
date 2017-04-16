package com.gft.isz.amdc;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.gft.isz.amdc.integration.database.Database;
import com.gft.isz.amdc.integration.geocoding.GeocodingClient;
import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Location;
import com.gft.isz.amdc.model.Shop;

@Component
public class AsyncEnrichenerImpl implements Enrichener {
	@Autowired
	private GeocodingClient geoClient;

	@Autowired
	@Qualifier("couchbase")
	private Database database;

	@Autowired
	private ExecutorService executorService; 
	
	@Override
	public void enrich(final Shop shop) {
		executorService.submit(new Runnable() {
			
			@Override
			public void run() {
				Address shopAddress = shop.getAddress();
				Location location = null;
				try {
					location = geoClient.getLocation(shopAddress.getPostCode());
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
		    	shopAddress.setLatitude(location.latitude);
		    	shopAddress.setLongitude(location.longitude);
				database.save(shop);
			}
		});
	}
}
