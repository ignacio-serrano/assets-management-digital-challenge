package com.gft.isz.amdc.integration.database;

import java.util.Collection;

import com.gft.isz.amdc.model.Shop;

public interface Database {

	Shop save(Shop shop);

	Shop retrieve(String name);

	Collection<Shop> retrieveAll();
}
