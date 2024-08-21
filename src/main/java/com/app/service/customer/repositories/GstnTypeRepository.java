package com.app.service.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.service.customer.entities.Gstntype;
import java.util.UUID;
/**
 * Repository for GSTNType
 */
@Repository
public interface GstnTypeRepository extends JpaRepository<Gstntype, UUID> {

}
