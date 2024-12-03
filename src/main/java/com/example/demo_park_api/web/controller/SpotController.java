package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.entity.ParkingSpots;
import com.example.demo_park_api.service.SpotService;
import com.example.demo_park_api.web.dto.ClientResponseDto;
import com.example.demo_park_api.web.dto.ParkingSpotDto;
import com.example.demo_park_api.web.dto.SpotResponseDto;
import com.example.demo_park_api.web.dto.mapper.SpotMapper;
import com.example.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@Tag(name = "All operations related parking spot management")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/spots")
public class SpotController {

    private final SpotService spotService;

    @Operation(summary = "Create a new spot", description = "resource for create a new spot" +
            " The Request requires a bearer token and is restricted to the 'ADMIN' role",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "resource created successfully",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL of the resource created")),
                    @ApiResponse(responseCode = "409", description = "Parking spot Already registered",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Entity unprocessed: Invalids or faulty data",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        ParkingSpots parkingSpots = SpotMapper.toSpot(parkingSpotDto);
        spotService.save(parkingSpots);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(parkingSpots.getCode())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Retrieve a parking spot", description = "resource for retrieved a parking spot by its code" +
            " The Request requires a bearer token and is restricted to the 'ADMIN' role",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = SpotResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Parking spot not found",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpotResponseDto> getByCode(@PathVariable String code) {
        ParkingSpots parkingSpots = spotService.getByCode(code);
        return ResponseEntity.ok(SpotMapper.spotResponseDto(parkingSpots));
    }
}
