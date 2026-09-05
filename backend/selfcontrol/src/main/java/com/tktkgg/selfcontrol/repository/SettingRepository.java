package com.tktkgg.selfcontrol.repository;

import java.util.Optional;
import java.util.UUID;

import com.tktkgg.selfcontrol.entity.Setting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, UUID> {
    Optional<Setting> findByUserId(UUID userId);
}
