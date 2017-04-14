package com.gft.isz.amdc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gft.isz.amdc.integration.database.Database;
import com.gft.isz.amdc.integration.geocoding.GeocodingClient;
import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Location;
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
	
	private final ObjectWriter writer = new ObjectMapper().writer();
	

	@Test
	public void getShops() throws Exception {
		
		when(database.retrieveAll()).thenReturn(Arrays.asList(new Shop[]{
				new Shop("Pet shop", new Address("10", "PL101AA", 50.3471439, -4.2186718)),
				new Shop("Book store", new Address("27", "PL23BZ", 50.3860506, -4.1567181)),
		}));
		
		Address expectedAddress = new Address();
    	expectedAddress.setNumber("27");
    	expectedAddress.setPostCode("PL23BZ");
    	expectedAddress.setLatitude(50.3860506);
    	expectedAddress.setLongitude(-4.1567181);
		
		mvc.perform(MockMvcRequestBuilders.get("/shops").accept(MediaType.APPLICATION_JSON)
				.param("latitude", "50.3860505").param("longitude", "-4.1567180"))
			.andExpect(status().isOk())
			.andExpect(content().json(writer.writeValueAsString(expectedAddress)));
	}
	
	@Test
	public void postShops_OK_EmptyDB() throws Exception {
		when(geoClient.getLocation(TEST_POST_CODE)).thenReturn(new Location(50.3471439, -4.2186718));
		when(database.save(any(Shop.class))).thenReturn(null);
		
		Shop shop = new Shop();
		shop.setName("Pet shop");
		shop.setAddress(new Address("1", TEST_POST_CODE));
		mvc.perform(MockMvcRequestBuilders.post("/shops").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(writer.writeValueAsString(shop)))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("")));
		
	}

}
