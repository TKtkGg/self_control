package com.tktkgg.selfcontrol.repository;

import com.tktkgg.selfcontrol.entity.User;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}