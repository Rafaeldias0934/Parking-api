package com.example.demo_park_api.service;

import com.example.demo_park_api.entity.User;
import com.example.demo_park_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }
    @Transactional
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!currentPassword.equals(confirmNewPassword)) {
           throw new RuntimeException("Passwords do not match. Please try again.");
        }
        
        User user = getById(id);
        if (!user.getPassword().equals(currentPassword)) {
            throw new RuntimeException("Your Password do not match. Please try again.");
        }

        user.setPassword(newPassword);
        return user;
    }

   @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
   }
}
