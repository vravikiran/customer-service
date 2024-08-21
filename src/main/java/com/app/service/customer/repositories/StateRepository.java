package com.app.service.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.service.customer.entities.State;
import java.util.UUID;
/**
 * Repository for State
 */
@Repository
public interface StateRepository extends JpaRepository<State, UUID> {

}
