package com.gft.isz.amdc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	
	@Test
	public void tdd() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/shops").param("latitude", "50.3471439").param("longitude", "-4.2186718").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("50.3471439,-4.2186718")));
	}
	
	@Test
	public void postShops_OK_EmptyDB() throws Exception {
		Shop shop = new Shop();
		shop.setName("Pet shop");
		shop.setAddress(new Address("1", "PL101AA"));
		mvc.perform(MockMvcRequestBuilders.post("/shops").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writer().writeValueAsString(shop)))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("")));
		
	}

	@Test
	@Ignore
	public void postShops_OK_UpdateShop() throws Exception {
		Shop shop = new Shop();
		shop.setName("Pet shop");
		shop.setAddress(new Address("2", "PL101AA"));
		mvc.perform(MockMvcRequestBuilders.post("/shops").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writer().writeValueAsString(shop)))
			.andExpect(status().isOk())
			.andExpect(content().json(""));
		
	}
}
