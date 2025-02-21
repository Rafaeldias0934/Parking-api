package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.entity.ClientSpot;
import com.example.demo_park_api.service.ParkingService;
import com.example.demo_park_api.web.dto.ParkedVehicleCreateDto;
import com.example.demo_park_api.web.dto.ParkedVehicleResponseDto;
import com.example.demo_park_api.web.dto.mapper.ClientSpotMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/parking")
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkedVehicleResponseDto> checkin(@RequestBody @Valid ParkedVehicleCreateDto parkedVehicleCreateDto) {
        ClientSpot clientSpot = ClientSpotMapper.toParkedVehicleCreateDto(parkedVehicleCreateDto);
        parkingService.ckeckIn(clientSpot);

        ParkedVehicleResponseDto responseDto = ClientSpotMapper.toParkedVehicleResponseDto(clientSpot);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(clientSpot.getReceipt())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);


    }
}
