package com.example.demo_park_api.service;

import com.example.demo_park_api.entity.ParkingSpots;
import com.example.demo_park_api.exception.CodeUniqueViolationException;
import com.example.demo_park_api.exception.EntityNotFoundException;
import com.example.demo_park_api.repositories.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo_park_api.entity.ParkingSpots.SpotStatus.AVAILABLE;

@RequiredArgsConstructor
@Service
public class SpotService {

    private final SpotRepository spotRepository;

    @Transactional
    public ParkingSpots save(ParkingSpots parkingSpots) {
        try {
            return spotRepository.save(parkingSpots);
        } catch (DataIntegrityViolationException ex) {
            throw new CodeUniqueViolationException(
                    String.format("The parking spot with code '%s' is already in use", parkingSpots.getCode())
            );
        }
    }

    @Transactional(readOnly = true)
    public ParkingSpots getByCode(String code) {
        return spotRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException(String.format("The parking spot with code '%s', not found", code))
        );
    }

    @Transactional(readOnly = true)
    public ParkingSpots getBySpotAvailable() {
        return spotRepository.findFirstBySpotStatus(AVAILABLE).orElseThrow(
                () -> new EntityNotFoundException("No Available spot was found")
        );
    }
}
