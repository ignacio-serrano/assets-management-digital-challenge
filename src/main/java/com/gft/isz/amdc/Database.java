package com.gft.isz.amdc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gft.isz.amdc.model.Shop;

@Component
/* Not really needed as it is the default scope. */
@Scope("singleton")
public class Database {
	private Map<String, Shop> db = new ConcurrentHashMap<>();
	
	public Shop save(Shop shop) {
		Shop prev = db.get(shop.getName());
		db.put(shop.getName(), shop);
		return prev;
	}
}
