package com.app.service.customer.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.service.customer.entities.Rating;
/**
 * Repository for Rating
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

}
