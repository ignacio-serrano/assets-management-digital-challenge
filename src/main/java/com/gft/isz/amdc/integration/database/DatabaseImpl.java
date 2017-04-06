package com.gft.isz.amdc.integration.database;

import java.util.Map;
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
	public Shop save(Shop shop) {
		Shop prev = db.get(shop.getName());
		db.put(shop.getName(), shop);
		return prev;
	}
}
