package com.gft.isz.amdc;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.JavaContext;
import com.couchbase.lite.Manager;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ExecutorService executorService() {
       return Executors.newFixedThreadPool(10);
    }
    
    /* Adds ETag cache control for GET operations. */
    @Bean
    public Filter shallowEtagHeaderFilter() {
      return new ShallowEtagHeaderFilter();
    }
    
    @Bean
    public Manager couchbaseManager() throws IOException {
    	Manager manager = new Manager(new JavaContext(), Manager.DEFAULT_OPTIONS);
    	manager.setStorageType("ForestDB");
		return manager;
    }
    
    @Bean
    public Database couchbaseDatabase(Manager couchbaseManager) throws CouchbaseLiteException {
    	return couchbaseManager.getDatabase("app");
    }
}
