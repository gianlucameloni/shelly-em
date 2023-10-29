package com.gmeloni.shelly.repository;

import com.gmeloni.shelly.model.HourlyEMEnergy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HourlyEMEnergyRepository extends CrudRepository<HourlyEMEnergy, LocalDateTime> {
}
