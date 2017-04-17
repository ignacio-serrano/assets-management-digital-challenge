package com.gft.isz.amdc.business;

import java.io.IOException;
import java.util.Collection;

import org.locationtech.spatial4j.distance.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gft.isz.amdc.integration.database.Database;
import com.gft.isz.amdc.integration.geocoding.GeocodingClient;
import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Shop;
import com.google.maps.errors.ApiException;

@Service
public class ShopsServiceImpl implements ShopsService {

	@Autowired
	private Database database;
	
	@Autowired
	private GeocodingClient geoClient;

    @Override
	public void createOrUpdate(Shop shop) throws ApiException, InterruptedException, IOException {
        database.save(shop);
        
    	geoClient.getLocationAsync(shop.getAddress().getPostCode(), location -> {
    		Address shopAddress = shop.getAddress();
	    	shopAddress.setLatitude(location.latitude);
	    	shopAddress.setLongitude(location.longitude);
			database.save(shop);
    	});
    }

    @Override
	public Address retrieveClosestShop(double latitude, double longitude) {
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

    @Override
    public Shop retrieveShopByName(String shopName) {
    	return database.retrieve(shopName);
    }
    
}
