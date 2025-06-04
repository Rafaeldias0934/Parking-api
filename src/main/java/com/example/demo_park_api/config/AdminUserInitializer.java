package com.example.demo_park_api.config;

import com.example.demo_park_api.entity.User;
import com.example.demo_park_api.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAdminUserIfNotExists() {
        String username = "admin@gmail.com";

        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("123456")); // senha criptografada
            user.setRole(User.Role.ROLE_ADMIN); // ou ROLE_ADMIN, se for Enum ou String
            userRepository.save(user);
            System.out.println("✅ Usuário admin@gmail.com criado no H2.");
        } else {
            System.out.println("ℹ️ Usuário admin@gmail.com já existe.");
        }
    }
}
