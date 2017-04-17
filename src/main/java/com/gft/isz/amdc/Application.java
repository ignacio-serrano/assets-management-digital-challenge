package com.gft.isz.amdc;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService executorService() {
       return Executors.newFixedThreadPool(10);
    }
    
    /* Adds ETag cache control for GET operations. */
    @Bean
    public Filter shallowEtagHeaderFilter() {
      return new ShallowEtagHeaderFilter();
    }
}
