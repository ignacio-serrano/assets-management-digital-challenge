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
	private static final Shop TEST_SHOP_1 = new Shop("Pet shop", new Address("1", "PL101AA", 50.3471439, -4.2186718));
	private static final Shop TEST_SHOP_2 = new Shop("Book store", new Address("27", "PL23BZ", 50.3860506, -4.1567181));

	@Autowired
	private MockMvc mvc;

	@MockBean
	private Database databaseMock;

	@MockBean
	private GeocodingClient geoClientMock;

	private final ObjectWriter writer = new ObjectMapper().writer();

	@Test
	public void getShops() throws Exception {

		when(databaseMock.retrieveAll())
				.thenReturn(Arrays.asList(new Shop[] { (Shop) TEST_SHOP_1.clone(), (Shop) TEST_SHOP_2.clone() }));

		Address expectedAddress = new Address();
		expectedAddress.setNumber("27");
		expectedAddress.setPostCode("PL23BZ");
		expectedAddress.setLatitude(50.3860506);
		expectedAddress.setLongitude(-4.1567181);

		mvc.perform(MockMvcRequestBuilders.get("/shops").accept(MediaType.APPLICATION_JSON)
				.param("latitude", "50.3860505").param("longitude", "-4.1567180")).andExpect(status().isOk())
				.andExpect(content().json(writer.writeValueAsString(TEST_SHOP_2.getAddress())));
	}

	@Test
	public void postShops_OK_EmptyDB() throws Exception {
		when(geoClientMock.getLocation(TEST_SHOP_1.getAddress().getPostCode())).thenReturn(
				new Location(TEST_SHOP_1.getAddress().getLatitude(), TEST_SHOP_1.getAddress().getLongitude()));
		when(databaseMock.retrieve(any(String.class))).thenReturn(null);

		Shop shop = new Shop();
		shop.setName(TEST_SHOP_1.getName());
		shop.setAddress(new Address(TEST_SHOP_1.getAddress().getNumber(), TEST_SHOP_1.getAddress().getPostCode()));
		mvc.perform(MockMvcRequestBuilders.post("/shops").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(writer.writeValueAsString(shop))).andExpect(status().isOk())
				.andExpect(content().string(equalTo("")));
	}

	@Test
	public void postShops_ERR_SomethingUnexpected() throws Exception {
		when(databaseMock.retrieve(any(String.class)))
				.thenThrow(new RuntimeException("Something unexpected happened."));

		Shop shop = new Shop();
		shop.setName("Pet shop");
		shop.setAddress(new Address("1", TEST_POST_CODE));
		mvc.perform(MockMvcRequestBuilders.post("/shops").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(writer.writeValueAsString(shop)))
				.andExpect(status().is(500)).andExpect(content().json("{\"errors\": [\"Unknown server error\"]}"));

	}
}
