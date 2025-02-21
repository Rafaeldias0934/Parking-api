package com.example.demo_park_api.repositories;

import com.example.demo_park_api.entity.ClientSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientSpotRepository extends JpaRepository<ClientSpot, Long> {
}
