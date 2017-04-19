package com.gft.isz.amdc.integration.database;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gft.isz.amdc.model.Shop;

/* All methods in this class clone parameters to avoid unintended 
 * modification of database contents by client code. */
@Component
/* Not really needed as it is the default scope. */
@Scope("singleton")
public class DatabaseImpl implements Database {

	/* According to the specification this will do, but for a real application I
	 * would have used Redis, MongoDB or CouchDB (see branch feature/couchbase).
	 * Current implementation only allows running an instance of the service and
	 * data is lost when the service is shutdown. */
	private Map<String, Shop> db = new ConcurrentHashMap<>();

	@Override
	public void save(Shop shop) {
		if (shop != null) {
			db.put(shop.getName(), (Shop) shop.clone());
		}
	}

	@Override
	public Shop retrieve(String name) {
		Shop shop = db.get(name);
		return shop != null ? (Shop) shop.clone() : null;
	}

	@Override
	public Collection<Shop> retrieveAll() {
		Collection<Shop> shops = db.values();
		Set<Shop> ret = new HashSet<>(shops.size());
		for (Shop shop : shops) {
			ret.add((Shop) shop.clone());
		}
		return ret;
	}
}
