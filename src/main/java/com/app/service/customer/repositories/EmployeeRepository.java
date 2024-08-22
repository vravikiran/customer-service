package com.app.service.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.service.customer.entities.Employee;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repostiory for employee - handles all database transactions related to
 * employee
 */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

}
