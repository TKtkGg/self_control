package com.tktkgg.selfcontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tktkgg.selfcontrol.entity.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByUserId(UUID userId);
}
