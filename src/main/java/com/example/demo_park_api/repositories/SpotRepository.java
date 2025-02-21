package com.example.demo_park_api.repositories;

import com.example.demo_park_api.entity.ParkingSpots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<ParkingSpots, Long> {

   Optional<ParkingSpots> findByCode(String code);

   Optional<ParkingSpots> findFirstByStatus(ParkingSpots.SpotStatus spotStatus);
}
