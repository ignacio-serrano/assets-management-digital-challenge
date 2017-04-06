package com.gft.isz.amdc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gft.isz.amdc.integration.geocoding.GeocodingClient;
import com.gft.isz.amdc.integration.geocoding.Location;
import com.gft.isz.amdc.model.Shop;

@RestController
public class Controller {
	
	@Autowired
	private Database database;
	
	@Autowired
	private GeocodingClient geoClient;

    @RequestMapping(value = "/shops", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getShops(@RequestParam(value="latitude") String latitude, @RequestParam(value="longitude") String longitude) {
        return latitude + "," + longitude;
    }
    
    @RequestMapping(value = "/shops", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Shop postShops(@RequestBody Shop shop) {
    	Location location = geoClient.getLocation(shop.getAddress().getPostCode());

    	shop.setLatitude(location.latitude);
    	shop.setLongitude(location.longitude);
    	
        return database.save(shop);
    }

}