package com.app.service.customer.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.service.customer.entities.Brand;
/**
 * Repository for Brand
 */
@Repository
public interface BrandRepository extends CrudRepository<Brand, UUID> {
	@Query(value = "select brandpk from Brand where brandname= :brandName")
	public UUID fetchByBrandName(@Param("brandName") String brandName);
}
