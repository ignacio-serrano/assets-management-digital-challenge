package com.gft.isz.amdc.integration.database;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gft.isz.amdc.model.Shop;

@Component
/* Not really needed as it is the default scope. */
@Scope("singleton")
public class DatabaseImpl implements Database {
	
	/* According to the specification this will do, but with more time I would 
	 * have used Redis or JasDB. */
	private Map<String, Shop> db = new ConcurrentHashMap<>();
	
	@Override
	public void save(Shop shop) {
		db.put(shop.getName(), shop);
	}

	@Override
	public Shop retrieve(String name) {
		/* Returning the same objects which are stored isn't very robust. With more time I would clone them. */
		return db.get(name);
	}

	@Override
	public Collection<Shop> retrieveAll() {
		/* Returning the same objects which are stored isn't very robust. With more time I would clone them. */
		Collection<Shop> shops = db.values();
		return shops;
	}
}
