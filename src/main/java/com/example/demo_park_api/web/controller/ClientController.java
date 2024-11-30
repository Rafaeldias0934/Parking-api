package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.entity.Client;
import com.example.demo_park_api.jwt.JwtUserDetails;
import com.example.demo_park_api.repositories.projection.ClientProjection;
import com.example.demo_park_api.service.ClientService;
import com.example.demo_park_api.service.UserService;
import com.example.demo_park_api.web.dto.ClientCreateDto;
import com.example.demo_park_api.web.dto.ClientResponseDto;
import com.example.demo_park_api.web.dto.PageableDto;
import com.example.demo_park_api.web.dto.UserResponseDto;
import com.example.demo_park_api.web.dto.mapper.ClientMapper;
import com.example.demo_park_api.web.dto.mapper.PageableMapper;
import com.example.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.List;

@Tag(name = "Clients", description = "All operations related to client management")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;


    @Operation(summary = "To create a new user", description = "Resource for create a new user, linked to an already registered user. " +
            "Request requires the use of a bearer token. Access is restricted to the 'CLIENT' role",
            security = @SecurityRequirement(name = "security"),
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

    @Operation(summary = "Get user by ID", description = "Resource for retrieving a user their unique ID. " +
            "Request requires the use of a bearer token. Access is restricted to the 'ADMIN' role",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieving successfully",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClientResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found. No user exists with the provided ID",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Resource access is forbidden to the CLIENT role",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> getById(@PathVariable Long id) {
        Client client = clientService.getById(id);
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }

    @Operation(summary = "Recover the client list", description = "Request requires the use of a bearer token. Access is restricted to the 'ADMIN' role",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representation of the page retrieved"
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                            description = "Representation of the entirety of the elements per page"
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id, asc")),
                            description = "Represents the ordering of the results. Multiple criteria sorting are supported")

            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieving successfully",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ClientResponseDto.class))
                    ),
                   @ApiResponse(responseCode = "403", description = "Resource access is forbidden to the CLIENT role",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                   )
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        Page<ClientProjection> clients = clientService.getAll(pageable);
        return ResponseEntity.ok(PageableMapper.pageableDto(clients));
    }

    @Operation(summary = "retrieving client details", description = "Resource for the client retrieve their data. " +
            "Request requires the use of a bearer token. Access is restricted to the 'CLIENT' role",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client details retrieved successfully",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClientResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Only users with 'Client' role, can access this resource",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/details")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> getClientDetails(@AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        Client client = clientService.getByUserId(jwtUserDetails.getId());
        if (client == null) {
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }
}
