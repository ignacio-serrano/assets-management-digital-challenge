package com.gft.isz.amdc;

import java.io.IOException;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.locationtech.spatial4j.distance.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gft.isz.amdc.integration.database.Database;
import com.gft.isz.amdc.integration.geocoding.GeocodingClient;
import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Location;
import com.gft.isz.amdc.model.Shop;
import com.google.maps.errors.ApiException;

@Validated
@RestController
public class Controller {
	
	@Autowired
	private Database database;
	
	@Autowired
	private GeocodingClient geoClient;

    @RequestMapping(value = "/shops", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Address getShops(@Min(-180) @Max(180) @RequestParam(value="latitude") double latitude, 
    		@Min(-180) @Max(180) @RequestParam(value="longitude") double longitude) {
    	Collection<Shop> shops = database.retrieveAll();
    	
    	Shop closestShop = null;
    	double closestDistance = Double.MAX_VALUE;
    	for (Shop currentShop : shops) {
    		if (closestShop == null) {
    			closestShop = currentShop;
    			closestDistance = DistanceUtils.distHaversineRAD(latitude, longitude, currentShop.getAddress().getLatitude(), currentShop.getAddress().getLongitude());
    		} else {
    			/* I wonder whether there is a method to choose the closest point without actually calculating the distance. */ 
    			double currentDistance = DistanceUtils.distHaversineRAD(latitude, longitude, currentShop.getAddress().getLatitude(), currentShop.getAddress().getLongitude());
    			if (currentDistance < closestDistance) {
    				closestShop = currentShop;
    				closestDistance = currentDistance;
    			}
    		}
    	}
    	
    	Address ret = null;
    	if (closestShop != null) {
	    	ret = closestShop.getAddress();
    	}
        return ret;
    }
    
    @RequestMapping(value = "/shops", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Shop postShops(@Valid @RequestBody Shop shop) throws ApiException, InterruptedException, IOException {
    	Address shopAddress = shop.getAddress();
    	Location location = geoClient.getLocation(shopAddress.getPostCode());

    	shopAddress.setLatitude(location.latitude);
    	shopAddress.setLongitude(location.longitude);
    	
        return database.save(shop);
    }

}