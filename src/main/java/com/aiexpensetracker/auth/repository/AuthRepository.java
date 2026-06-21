package com.aiexpensetracker.auth.repository;

import com.aiexpensetracker.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Users, UUID> {

    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}
