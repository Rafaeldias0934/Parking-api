package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.entity.Client;
import com.example.demo_park_api.jwt.JwtUserDetails;
import com.example.demo_park_api.service.ClientService;
import com.example.demo_park_api.service.UserService;
import com.example.demo_park_api.web.dto.ClientCreateDto;
import com.example.demo_park_api.web.dto.ClientResponseDto;
import com.example.demo_park_api.web.dto.UserResponseDto;
import com.example.demo_park_api.web.dto.mapper.ClientMapper;
import com.example.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Clients", description = "All operations related to client management")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;


    @Operation(summary = "To create a new user", description = "Resource for create a new user, linked to an already registered user. " +
            "Request requires the use of a bearer token. Access is restricted to the 'CLIENT' role",
            responses = {
                    @ApiResponse(responseCode = "201", description = "resource created successfully",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClientResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "User CPF is already registered in the System",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource could not be processed due to invalid input data. Please check the request parameters",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Resource access is forbidden to the ADMIN role",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> create(@RequestBody @Valid ClientCreateDto clientCreateDto,
                                                    @AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        Client client = ClientMapper.toClient(clientCreateDto);
        client.setUser(userService.getById(jwtUserDetails.getId()));
        clientService.toSave(client);

        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toDto(client));
    }
}
