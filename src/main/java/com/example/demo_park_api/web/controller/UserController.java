package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.entity.User;
import com.example.demo_park_api.service.UserService;
import com.example.demo_park_api.web.dto.UserCreateDto;
import com.example.demo_park_api.web.dto.UserPasswordDto;
import com.example.demo_park_api.web.dto.UserResponseDto;
import com.example.demo_park_api.web.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody UserCreateDto userCreateDto) {
        User savedUser = userService.saveUser(UserMapper.toUser(userCreateDto));
       return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(savedUser));

    }

   @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
       User getOneUser = userService.getById(id);
       return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDto(getOneUser));
   }

   @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updatePassoword(@PathVariable Long id, @RequestBody UserPasswordDto userPasswordDto) {
        User savedPassword = userService.updatePassword(id, userPasswordDto.getCurrentPassword(), userPasswordDto.getNewPassword(), userPasswordDto.getConfirmNewPassword());
        return ResponseEntity.ok(UserMapper.toDto(savedPassword));
   }

   @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
       List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
   }
}
