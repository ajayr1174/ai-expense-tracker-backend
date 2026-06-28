package com.aiexpensetracker.user.repository;

import com.aiexpensetracker.user.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, UUID> {
    Optional<UserPreference> findByUserId(UUID userId);

    void deleteById(UUID userId);
}