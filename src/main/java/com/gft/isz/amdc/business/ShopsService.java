package com.gft.isz.amdc.business;

import java.io.IOException;

import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Shop;
import com.google.maps.errors.ApiException;

public interface ShopsService {

	void createOrUpdate(Shop shop) throws ApiException, InterruptedException, IOException;

	Address retrieveClosestShop(double latitude, double longitude);

	Shop retrieveShopByName(String shopName);

}