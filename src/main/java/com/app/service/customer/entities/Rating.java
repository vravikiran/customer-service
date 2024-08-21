package com.app.service.customer.entities;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
/**
 * Rating entity
 */
@Entity
public class Rating {
	@Id
	private UUID ratingid;
	private String rating;

	public UUID getRatingid() {
		return ratingid;
	}

	public String getRating() {
		return rating;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rating, ratingid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rating other = (Rating) obj;
		return Objects.equals(rating, other.rating) && Objects.equals(ratingid, other.ratingid);
	}

	@Override
	public String toString() {
		return "Rating [ratingid=" + ratingid + ", rating=" + rating + "]";
	}
}
