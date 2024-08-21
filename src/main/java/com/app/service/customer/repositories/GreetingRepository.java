package com.app.service.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.service.customer.entities.Greeting;
import java.util.UUID;
/**
 * Repository for Greeting
 */
@Repository
public interface GreetingRepository extends JpaRepository<Greeting, UUID> {

}
