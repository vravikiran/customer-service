package com.app.service.customer.entities;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Brand {
	@Id
	private UUID brandpk;
	private String brandcode;
	private String brandname;

	public UUID getBrandpk() {
		return brandpk;
	}

	public void setBrandpk(UUID brandpk) {
		this.brandpk = brandpk;
	}

	public String getBrandcode() {
		return brandcode;
	}

	public void setBrandcode(String brandcode) {
		this.brandcode = brandcode;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	@Override
	public int hashCode() {
		return Objects.hash(brandcode, brandname, brandpk);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Brand other = (Brand) obj;
		return Objects.equals(brandcode, other.brandcode) && Objects.equals(brandname, other.brandname)
				&& Objects.equals(brandpk, other.brandpk);
	}

	@Override
	public String toString() {
		return "Brand [brandpk=" + brandpk + ", brandcode=" + brandcode + ", brandname=" + brandname + "]";
	}
}
