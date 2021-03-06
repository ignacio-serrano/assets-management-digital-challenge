package com.gft.isz.amdc;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gft.isz.amdc.business.ShopsService;
import com.gft.isz.amdc.model.Address;
import com.gft.isz.amdc.model.HATEOASAddress;
import com.gft.isz.amdc.model.Shop;
import com.google.maps.errors.ApiException;

/* Not much to comment on this class. I'm using JSR-303 (Bean Validator) to check input values. */
@Validated
@RestController
public class Controller {

	@Autowired
	private ShopsService shopsService;

	@RequestMapping(value = "/shops", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HATEOASAddress getShops(@Min(-180) @Max(180) @RequestParam(value = "latitude") double latitude,
			@Min(-180) @Max(180) @RequestParam(value = "longitude") double longitude) {
		Address address = shopsService.retrieveClosestShop(latitude, longitude);
		HATEOASAddress ret = new HATEOASAddress();
		ret.setContent(address);
		ret.add(linkTo(methodOn(Controller.class).getShops(address.getLatitude(), address.getLongitude())).withSelfRel());
		return ret;
	}

	@RequestMapping(value = "/shops", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Shop> postShops(@Valid @RequestBody Shop shop) throws ApiException, InterruptedException, IOException {
		Shop previousShop = shopsService.createOrUpdate(shop);
		return new ResponseEntity<Shop>(previousShop, previousShop == null ? HttpStatus.CREATED : HttpStatus.OK);
	}

}