package com.gft.isz.amdc.model;

import org.springframework.hateoas.ResourceSupport;

public class HATEOASAddress extends ResourceSupport {
	private Address content;

	public Address getContent() {
		return content;
	}

	public void setContent(Address content) {
		this.content = content;
	}
}
