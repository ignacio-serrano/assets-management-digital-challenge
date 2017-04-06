package com.gft.isz.amdc;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.isz.amdc.integration.database.Database;
import com.gft.isz.amdc.integration.geocoding.GeocodingClient;
import com.gft.isz.amdc.integration.geocoding.Location;
import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
	
	private static final String TEST_POST_CODE = "PL101AA";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private Database database;
	
	@MockBean
	private GeocodingClient geoClient;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void tdd() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/shops").param("latitude", "50.3471439").param("longitude", "-4.2186718").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("50.3471439,-4.2186718")));
	}
	
	@Test
	public void postShops_OK_EmptyDB() throws Exception {
		when(geoClient.getLocation(TEST_POST_CODE)).thenReturn(new Location(50.3471439, -4.2186718));
		when(database.save(any(Shop.class))).thenReturn(null);
		
		Shop shop = new Shop();
		shop.setName("Pet shop");
		shop.setAddress(new Address("1", TEST_POST_CODE));
		mvc.perform(MockMvcRequestBuilders.post("/shops").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writer().writeValueAsString(shop)))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("")));
		
	}

}
