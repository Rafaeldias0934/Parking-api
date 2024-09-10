package com.example.demo_park_api.web.controller;

import com.example.demo_park_api.entity.User;
import com.example.demo_park_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
       return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

   @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
       User getOneUser = userService.getById(id);
       return ResponseEntity.status(HttpStatus.OK).body(getOneUser);
   }

   @PatchMapping("/{id}")
    public ResponseEntity updatePassoword(@PathVariable Long id, @RequestBody User user) {
        User savedPassword = userService.updatePassword(id, user.getPassword());
        return ResponseEntity.ok(savedPassword);
   }

   @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
       List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
   }
}
