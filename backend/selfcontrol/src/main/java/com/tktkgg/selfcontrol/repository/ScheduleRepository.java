package com.tktkgg.selfcontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tktkgg.selfcontrol.entity.Schedule;
import java.util.UUID;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByUserId(UUID userId);
}
