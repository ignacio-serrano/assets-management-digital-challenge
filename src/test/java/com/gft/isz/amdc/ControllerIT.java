package com.gft.isz.amdc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIT {
	
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private Shop doPostAndAssertOK(Shop shop) {
    	ResponseEntity<Shop> response = restTemplate.exchange("http://localhost:" + port + "/shops", HttpMethod.POST, new HttpEntity<Shop>(shop), Shop.class);
    	assertThat(response.getStatusCode(), is(HttpStatus.OK));
    	return response.getBody();
    }
    
    private Map<String, Object> doPostAndAssertError(Shop shop, HttpStatus... anyOf) {
    	ResponseEntity<Map<String, Object>> response = restTemplate.exchange("http://localhost:" + port + "/shops", HttpMethod.POST, new HttpEntity<Shop>(shop), new ParameterizedTypeReference<Map<String, Object>>(){});
    	boolean statusAssertion = false;
    	for (HttpStatus expectedStatus : anyOf) {
    		if (response.getStatusCode().equals(expectedStatus)) {
    			statusAssertion = true;
    		}
    	}
    	if (!statusAssertion) {
    		throw new AssertionError("Response status code wasn't in: " + anyOf);
    	}
    	return response.getBody();
    }
    
    private Address doGetAndAssertOK(double latitude, double longitude) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/shops")
				.queryParam("latitude", latitude)
				.queryParam("longitude", longitude);
		ResponseEntity<Address> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Address.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));

    	return response.getBody();
    }
    
    private Map<String, Object> doGetAndAssertError(double latitude, double longitude, HttpStatus... anyOf) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/shops")
				.queryParam("latitude", latitude)
				.queryParam("longitude", longitude);
		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>(){});
		
    	boolean statusAssertion = false;
    	for (HttpStatus expectedStatus : anyOf) {
    		if (response.getStatusCode().equals(expectedStatus)) {
    			statusAssertion = true;
    		}
    	}
    	if (!statusAssertion) {
    		throw new AssertionError("Response status code wasn't in: " + anyOf);
    	}
    	
    	return response.getBody();
    }
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void doublePost_OK() {
    	
    	Shop request1 = new Shop();
    	request1.setName("Pet shop");
    	request1.setAddress(new Address("1", "PL101AA"));
		
    	Shop response1 = doPostAndAssertOK(request1);
    	
    	assertThat(response1, is(nullValue()));
    	
    	Shop request2 = new Shop();
    	request2.setName("Pet shop");
    	request2.setAddress(new Address("17", "PL23BZ"));
    	
    	Shop response2 = doPostAndAssertOK(request2);
    	assertThat(response2.getName(), equalTo(request1.getName()));
    	assertThat(response2.getAddress().getNumber(), equalTo(request1.getAddress().getNumber()));
    	assertThat(response2.getAddress().getPostCode(), equalTo(request1.getAddress().getPostCode()));
    }
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void postThenGet_OK() {
    	Shop request1 = new Shop();
    	request1.setName("Pet shop");
    	request1.setAddress(new Address("1", "PL101AA"));
		
    	Shop response1 = doPostAndAssertOK(request1);
    	
    	assertThat(response1, is(nullValue()));
    	
    	Address responseBody = doGetAndAssertOK(50.3471439, -4.2186718);
    	assertThat(responseBody, is(notNullValue()));
    	assertThat(responseBody.getNumber(), is("1"));
    	assertThat(responseBody.getPostCode(), is("PL101AA"));
    	assertThat(responseBody.getLatitude(), is(notNullValue()));
    	assertThat(responseBody.getLongitude(), is(notNullValue()));
    }
    
    @Test
    public void postShops_ERR_NoName() {
    	Shop request1 = new Shop();
    	request1.setName("");
    	request1.setAddress(new Address("1", "PL101AA"));
		
    	Map<String, Object> response = doPostAndAssertError(request1, HttpStatus.BAD_REQUEST);
    	List<String> errors = (List<String>) response.get("errors");
    	assertThat(errors, is(notNullValue()));
    	assertThat(errors, containsInAnyOrder("shop.name may not be empty"));
    }

    @Test
    public void postShops_ERR_NoAddress() {
    	Shop request1 = new Shop();
    	request1.setName("Pet shop");
    	request1.setAddress(null);
		
    	Map<String, Object> response = doPostAndAssertError(request1, HttpStatus.BAD_REQUEST);
    	List<String> errors = (List<String>) response.get("errors");
    	assertThat(errors, is(notNullValue()));
    	assertThat(errors, containsInAnyOrder("shop.address may not be null"));
    }
    
    @Test
    public void postShops_ERR_NoAddressNumber() {
    	Shop request1 = new Shop();
    	request1.setName("Pet shop");
    	request1.setAddress(new Address(null, "PL101AA"));
		
    	Map<String, Object> response = doPostAndAssertError(request1, HttpStatus.BAD_REQUEST);
    	List<String> errors = (List<String>) response.get("errors");
    	assertThat(errors, is(notNullValue()));
    	assertThat(errors, containsInAnyOrder("shop.address.number may not be empty"));
    }

    @Test
    public void postShops_ERR_NoAddressPostCode() {
    	Shop request1 = new Shop();
    	request1.setName("Pet shop");
    	request1.setAddress(new Address("1", null));
		
    	Map<String, Object> response = doPostAndAssertError(request1, HttpStatus.BAD_REQUEST);
    	List<String> errors = (List<String>) response.get("errors");
    	assertThat(errors, is(notNullValue()));
    	assertThat(errors, containsInAnyOrder("shop.address.postCode may not be empty"));
    }

    @Test
    public void getShops_ERR_TooSmall() {
    	Map<String, Object> response = doGetAndAssertError(-200, -4.2186718, HttpStatus.BAD_REQUEST);
    	List<String> errors = (List<String>) response.get("errors");
    	assertThat(errors, is(notNullValue()));
    	assertThat(errors, containsInAnyOrder("getShops.latitude must be greater than or equal to -180. Was -200.0"));
    }

    @Test
    public void getShops_ERR_TooBig() {
    	Map<String, Object> response = doGetAndAssertError(200, -4.2186718, HttpStatus.BAD_REQUEST);
    	List<String> errors = (List<String>) response.get("errors");
    	assertThat(errors, is(notNullValue()));
    	assertThat(errors, containsInAnyOrder("getShops.latitude must be less than or equal to 180. Was 200.0"));
    }
}
