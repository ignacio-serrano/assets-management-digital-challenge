package com.gft.isz.amdc.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gft.isz.amdc.integration.database.Database;
import com.gft.isz.amdc.integration.geocoding.GeocodingClient;
import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.Shop;

public class ShopsServiceImplTest {

	private static final Shop TEST_SHOP_1 = new Shop("Pet shop", new Address("1", "PL101AA", 50.3471439, -4.2186718));
	private static final Shop TEST_SHOP_2 = new Shop("Book store", new Address("27", "PL23BZ", 50.3860506, -4.1567181));

	@InjectMocks
	private ShopsServiceImpl testTarget = new ShopsServiceImpl();

	@Mock
	private Database databaseMock;
	
	@Mock
	private GeocodingClient geoClientMock;

	@Before
	public void beforeShopsServiceImplTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void retrieveClosestShop_OK() {
		when(databaseMock.retrieveAll()).thenReturn(
				Arrays.asList(new Shop[] { (Shop) TEST_SHOP_1.clone(), (Shop) TEST_SHOP_2.clone()}));

		Address testReturn = testTarget.retrieveClosestShop(50.3860505, -4.1567180);
		
		assertThat(testReturn, is(equalTo(TEST_SHOP_2.getAddress())));
	}

	@Test
	public void retrieveShopByName_OK() {
		when(databaseMock.retrieve(TEST_SHOP_1.getName())).thenReturn(TEST_SHOP_1);
		Shop testReturn = testTarget.retrieveShopByName(TEST_SHOP_1.getName());

		assertThat(testReturn, is(equalTo(TEST_SHOP_1)));
	}
}
