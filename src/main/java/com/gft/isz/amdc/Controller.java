package com.gft.isz.amdc;

import java.io.IOException;
import java.util.Collection;

import org.locationtech.spatial4j.distance.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gft.isz.amdc.integration.database.Database;
import com.gft.isz.amdc.integration.geocoding.GeocodingClient;
import com.gft.isz.amdc.model.ExtendedAddress;
import com.gft.isz.amdc.model.Location;
import com.gft.isz.amdc.model.Shop;
import com.google.maps.errors.ApiException;

@RestController
public class Controller {
	
	@Autowired
	private Database database;
	
	@Autowired
	private GeocodingClient geoClient;

    @RequestMapping(value = "/shops", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExtendedAddress getShops(@RequestParam(value="latitude") double latitude, @RequestParam(value="longitude") double longitude) {
    	Collection<Shop> shops = database.retrieveAll();
    	
    	Shop closestShop = null;
    	double closestDistance = Double.MAX_VALUE;
    	for (Shop currentShop : shops) {
    		if (closestShop == null) {
    			closestShop = currentShop;
    			closestDistance = DistanceUtils.distHaversineRAD(latitude, longitude, currentShop.getLatitude(), currentShop.getLongitude());
    		} else {
    			double currentDistance = DistanceUtils.distHaversineRAD(latitude, longitude, currentShop.getLatitude(), currentShop.getLongitude());
    			if (currentDistance < closestDistance) {
    				closestShop = currentShop;
    				closestDistance = currentDistance;
    			}
    		}
    	}
    	
    	ExtendedAddress ret = null;
    	if (closestShop != null) {
	    	ret = new ExtendedAddress();
	    	ret.setNumber(closestShop.getAddress().getNumber());
	    	ret.setPostCode(closestShop.getAddress().getPostCode());
	    	ret.setLocation(new Location(closestShop.getLatitude(), closestShop.getLongitude()));
    	}
        return ret;
    }
    
    @RequestMapping(value = "/shops", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Shop postShops(@RequestBody Shop shop) throws ApiException, InterruptedException, IOException {
    	Location location = geoClient.getLocation(shop.getAddress().getPostCode());

    	shop.setLatitude(location.latitude);
    	shop.setLongitude(location.longitude);
    	
        return database.save(shop);
    }

}