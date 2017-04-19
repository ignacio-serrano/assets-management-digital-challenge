package com.gft.isz.amdc.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Shop implements Cloneable {

	@JsonProperty("shopName")
	@NotEmpty
	private String name;

	@JsonProperty("shopAddress")
	@NotNull
	@Valid
	private Address address;

	public Shop() {
	}

	public Shop(String name, Address address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj.getClass() != this.getClass()) {
			return false;
		}

		boolean ret = true;

		if (obj instanceof Shop) {
			Shop that = (Shop) obj;
			if (this.getName() != null) {
				ret = this.getName().equals(that.getName());
			} else {
				ret = that.getName() == null;
			}

			if (ret) {
				if (this.getAddress() != null) {
					ret = this.getAddress().equals(that.getAddress());
				} else {
					ret = this.getAddress() == null;
				}
			}
		} else {
			ret = false;
		}

		return ret;
	}

	@Override
	public Object clone() {
		Shop clone;
		try {
			clone = (Shop) super.clone();
		} catch (CloneNotSupportedException e) {
			/* This can't actually happen unless someone changes base class. If
			 * that's the case, better warn the programmer. */
			throw new RuntimeException("Did you forget to override clone() in base class?", e);
		}
		clone.address = this.address != null ? (Address) this.address.clone() : null;
		return clone;
	}
}
