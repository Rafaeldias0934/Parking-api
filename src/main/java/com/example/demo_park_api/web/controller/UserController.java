package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.entity.User;
import com.example.demo_park_api.service.UserService;
import com.example.demo_park_api.web.dto.UserCreateDto;
import com.example.demo_park_api.web.dto.UserPasswordDto;
import com.example.demo_park_api.web.dto.UserResponseDto;
import com.example.demo_park_api.web.dto.mapper.UserMapper;
import com.example.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Handles user management by allowing registration of new users, retrieval of user information, updates to user details, and deletion of users from the system.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "Resource for create a new user",
            responses = {
            @ApiResponse(responseCode = "201", description = "resource created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "User email is already registered in the System",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        User savedUser = userService.saveUser(UserMapper.toUser(userCreateDto));
       return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(savedUser));

    }

    @Operation(summary = "recover a user by id", description = "Recover user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "resource recovered successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
   @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
       User getOneUser = userService.getById(id);
       return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDto(getOneUser));
   }

    @Operation(summary = "Update password", description = "update password",
            responses = {
                    @ApiResponse(responseCode = "204", description = "updated password successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "Password not to match",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Fields Invalids or not formatted",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
   @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassoword(@PathVariable Long id,@Valid @RequestBody UserPasswordDto userPasswordDto) {
        User savedPassword = userService.updatePassword(id, userPasswordDto.getCurrentPassword(), userPasswordDto.getNewPassword(), userPasswordDto.getConfirmNewPassword());
        return ResponseEntity.noContent().build();
   }

    @Operation(summary = "List all users", description = "List all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List with all users registered",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
            }
    )
   @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
       List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(UserMapper.dtoList(users));
   }
}
