package com.app.service.customer.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Creditstatus {
	@Id
	private UUID creditstatusid;
	private String creditstatus;

	public UUID getCreditstatusid() {
		return creditstatusid;
	}

	public void setCreditstatusid(UUID creditstatusid) {
		this.creditstatusid = creditstatusid;
	}

	public String getCreditstatus() {
		return creditstatus;
	}

	public void setCreditstatus(String creditstatus) {
		this.creditstatus = creditstatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creditstatus, creditstatusid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Creditstatus other = (Creditstatus) obj;
		return Objects.equals(creditstatus, other.creditstatus) && Objects.equals(creditstatusid, other.creditstatusid);
	}

	@Override
	public String toString() {
		return "CreditStatus [creditstatusid=" + creditstatusid + ", creditstatus=" + creditstatus + "]";
	}

}
