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

	/* A create or update method is not very common (quite often there are
	 * functional differences between both processes) but in this case it saves
	 * a lot of extra work. */
	@Override
	public Shop createOrUpdate(Shop shop) throws ApiException, InterruptedException, IOException {
		
		/* Shop is saved beforehand so data isn't lost in case Google Maps isn't
		 * available. */
		Shop ret = database.save(shop);

		geoClient.getLocationAsync(shop.getAddress().getPostCode(), location -> {
			/* This is run asynchronously, so the process can continue
			 * meanwhile. */
			Address shopAddress = shop.getAddress();
			shopAddress.setLatitude(location.latitude);
			shopAddress.setLongitude(location.longitude);
			database.save(shop);
		});
		
		return ret;
	}

	/* The algorithm use to choose the closest shop is to traverse the whole
	 * database and calculate the distance between the specified location and
	 * the shop. The distance is calculated as the crow flies, which doesn't
	 * usually make sense (the average user doesn't fly ;). With more time to
	 * dig in the Google Maps API I would calculate a more useful distance. */
	@Override
	public Address retrieveClosestShop(double latitude, double longitude) {
		Collection<Shop> shops = database.retrieveAll();

		Shop closestShop = null;
		double closestDistance = Double.MAX_VALUE;
		for (Shop currentShop : shops) {
			if (closestShop == null) {
				closestShop = currentShop;
				/* I found an already existing and tested library and used it
				 * instead of implementing Haversine method by myself. */
				closestDistance = DistanceUtils.distHaversineRAD(latitude, longitude,
						currentShop.getAddress().getLatitude(), currentShop.getAddress().getLongitude());
			} else {
				/* I wonder whether there is a method to choose the closest
				 * point without actually calculating the distance. */
				double currentDistance = DistanceUtils.distHaversineRAD(latitude, longitude,
						currentShop.getAddress().getLatitude(), currentShop.getAddress().getLongitude());
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

}
