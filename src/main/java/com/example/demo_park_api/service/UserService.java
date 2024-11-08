package com.example.demo_park_api.service;

import com.example.demo_park_api.entity.User;
import com.example.demo_park_api.exception.EntityNotFoundException;
import com.example.demo_park_api.exception.PasswordInvalidException;
import com.example.demo_park_api.exception.UsernameUniqueViolationException;
import com.example.demo_park_api.exception.ValidateCurrentPasswordException;
import com.example.demo_park_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("{%s} creation failed: this user is already registered.",user.getUsername()));
        }
    }


    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User of ID = {%d}, not found", id))
        );
    }
    @Transactional
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
           throw new PasswordInvalidException( String.format("Passwords for user with ID = {%s} do not match. Please try again.", id));
        }
        
        User user = getById(id);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordInvalidException(String.format("Your current Password for user with ID = {%s}, does not match. Please try again.",id));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

   @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
   }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with username = {%s}, not found", username))
        );
    }

    @Transactional(readOnly = true)
    public User.Role findRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }
}
