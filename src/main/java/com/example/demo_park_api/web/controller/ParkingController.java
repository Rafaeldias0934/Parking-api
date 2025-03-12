package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.entity.ClientSpot;
import com.example.demo_park_api.service.ParkingService;
import com.example.demo_park_api.web.dto.ParkedVehicleCreateDto;
import com.example.demo_park_api.web.dto.ParkedVehicleResponseDto;
import com.example.demo_park_api.web.dto.mapper.ClientSpotMapper;
import com.example.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

    @Operation(summary = "operation in the Check-in", description = "Feature to register a vehicle in the parking lot. " +
    "Resource requires a bearer token, access restricted to Role='ADMIN'.",
    security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(responseCode = "201", description = "Resource created successfully",
                    headers = @Header(name = HttpHeaders.LOCATION, description = "URL in the access the a resource created"),
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ParkedVehicleResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Possible causes: <br/>" +
                    "- CPF in the client no registered on system; <br/> +" +
                    "No available spot was found;",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "No resource were processed due to lack of data or invalids data",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "No allowed resource fot the Client role",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                    schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkedVehicleResponseDto> checkIn(@RequestBody @Valid ParkedVehicleCreateDto parkedVehicleCreateDto) {
        ClientSpot clientSpot = ClientSpotMapper.toParkedVehicleCreateDto(parkedVehicleCreateDto);
        parkingService.checkIn(clientSpot);
        ParkedVehicleResponseDto responseDto = ClientSpotMapper.toParkedVehicleResponseDto(clientSpot);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(clientSpot.getReceipt())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }
}
