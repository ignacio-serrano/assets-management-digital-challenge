package com.gft.isz.amdc;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.locationtech.spatial4j.distance.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
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
	@Qualifier("couchbase")
	private Database database;
	
	@Autowired
	private Enrichener enrichener;
	
	@Autowired
	private com.couchbase.lite.Database couchdb;
	
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
    	Shop previousShop = database.retrieve(shop.getName());
        database.save(shop);
        
    	enrichener.enrich(shop);
    	
        return previousShop;
    }

    @RequestMapping(value = "/db", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String post(@RequestBody Map<String, Object> requestBody) throws CouchbaseLiteException {
    	Document document = couchdb.createDocument();
		document.putProperties(requestBody);
		return document.getId();
    }

    @RequestMapping(value = "/db", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> get(@RequestParam(value="id") String id) {
    	Document document = couchdb.getDocument(id);
    	return document.getProperties();
    }
}