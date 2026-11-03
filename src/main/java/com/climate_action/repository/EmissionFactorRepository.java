package com.climate_action.repository;

import com.climate_action.entity.EmissionFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmissionFactorRepository
        extends JpaRepository<EmissionFactor, Long> {
}