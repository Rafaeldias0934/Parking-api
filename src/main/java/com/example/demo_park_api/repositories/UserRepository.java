package com.example.demo_park_api.repositories;

import com.example.demo_park_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
