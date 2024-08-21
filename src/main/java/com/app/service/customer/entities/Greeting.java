package com.app.service.customer.entities;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
/**
 * Greeting entity
 */
@Entity
public class Greeting {
	@Id
	private UUID greetingid;
	private String greetingcode;
	private String greetingdesc;

	public UUID getGreetingid() {
		return greetingid;
	}

	public void setGreetingid(UUID greetingid) {
		this.greetingid = greetingid;
	}

	public String getGreetingcode() {
		return greetingcode;
	}

	public void setGreetingcode(String greetingcode) {
		this.greetingcode = greetingcode;
	}

	public String getGreetingdesc() {
		return greetingdesc;
	}

	public void setGreetingdesc(String greetingdesc) {
		this.greetingdesc = greetingdesc;
	}

	@Override
	public int hashCode() {
		return Objects.hash(greetingcode, greetingdesc, greetingid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Greeting other = (Greeting) obj;
		return Objects.equals(greetingcode, other.greetingcode) && Objects.equals(greetingdesc, other.greetingdesc)
				&& Objects.equals(greetingid, other.greetingid);
	}

	@Override
	public String toString() {
		return "Greeting [greetingid=" + greetingid + ", greetingcode=" + greetingcode + ", greetingdesc="
				+ greetingdesc + "]";
	}
}
