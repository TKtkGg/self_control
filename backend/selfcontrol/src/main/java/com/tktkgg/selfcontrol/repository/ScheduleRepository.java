package com.tktkgg.selfcontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tktkgg.selfcontrol.entity.Schedule;
import java.util.UUID;
import java.util.List;
import java.time.DayOfWeek;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByUserId(UUID userId);
    Optional<Schedule> findByUserIdAndDayOfWeek(UUID userId, DayOfWeek dayOfWeek);
}
