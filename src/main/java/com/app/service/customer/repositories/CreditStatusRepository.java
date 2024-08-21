package com.app.service.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.service.customer.entities.Creditstatus;

import java.util.UUID;
/**
 * Repository for CreditStatus
 */
@Repository
public interface CreditStatusRepository extends JpaRepository<Creditstatus, UUID> {

}
