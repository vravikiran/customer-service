package com.app.service.customer.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.service.customer.entities.Dataimport;
/**
 * Repository for Data Import
 */
@Repository
public interface DataImportRepository extends JpaRepository<Dataimport, UUID> {

}
