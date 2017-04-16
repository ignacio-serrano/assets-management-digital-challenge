package com.gft.isz.amdc.integration.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.isz.amdc.model.Shop;

@Component("couchbase")
public class CouchbaseImpl implements Database {

	@Autowired
	private com.couchbase.lite.Database db;

	//TODO: Initialize somewhere else.
	private final ObjectMapper om = new ObjectMapper();
	
	@Override
	public void save(Shop shop) {
		Document document = db.getDocument(shop.getName());
		Map<String, Object> properties = om.convertValue(shop, new TypeReference<Map<String, Object>>() {});
		try {
			document.putProperties(properties);
		} catch (CouchbaseLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Shop retrieve(String name) {
		Document document = db.getExistingDocument(name);
		return om.convertValue(document.getProperties(), Shop.class);
	}

	@Override
	public Collection<Shop> retrieveAll() {
		Query allDocumentsQuery = db.createAllDocumentsQuery();
		List<Shop> ret = null;
		try {
			QueryEnumerator results = allDocumentsQuery.run();
			ret = new ArrayList<>(results.getCount());
			while (results.hasNext()) {
				QueryRow row = results.next();
				ret.add(om.convertValue(row.getDocument().getProperties(), Shop.class));
			}
		} catch (CouchbaseLiteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ret;
	}

}
