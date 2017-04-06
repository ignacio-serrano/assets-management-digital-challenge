package com.gft.isz.amdc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIT {
	
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
 
    private Shop doPost(Shop shop) {
    	ResponseEntity<Shop> response = restTemplate.exchange("http://localhost:" + port + "/shops", HttpMethod.POST, new HttpEntity<Shop>(shop), Shop.class);
    	return response.getBody();
    }
    
    @Test
    public void doublePost() {
    	
    	Shop request1 = new Shop();
    	request1.setName("Pet shop");
    	request1.setAddress(new Address("1", "PL101AA"));
		
    	Shop response1 = doPost(request1);
    	
    	assertThat(response1, is(nullValue()));
    	
    	Shop request2 = new Shop();
    	request2.setName("Pet shop");
    	request2.setAddress(new Address("17", "PL23BZ"));
    	
    	Shop response2 = doPost(request2);
    	assertThat(response2, equalTo(request1));
    	
    }
}
