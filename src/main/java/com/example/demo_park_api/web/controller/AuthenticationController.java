package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.jwt.JwtToken;
import com.example.demo_park_api.jwt.JwtUserDetailsService;
import com.example.demo_park_api.web.dto.UserResponseDto;
import com.example.demo_park_api.web.dto.UserSignInDto;
import com.example.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Authentication", description = "resource of proceed with the authentication on the API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Authenticate With the API", description = "Resource to authenticate on the API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication completed successfully returning a bearer token",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "invalid input fields",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody @Valid UserSignInDto dto, HttpServletRequest request) {
        log.info("Process of authentication by login {}", dto.getUsername());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = jwtUserDetailsService.getTokenAuthenticated(dto.getUsername());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            log.warn("Bad Credentials from username '{}'", dto.getUsername());
        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST,"Credentials Invalids"));
    }
}
