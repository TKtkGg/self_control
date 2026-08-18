package com.tktkgg.selfcontrol.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tktkgg.selfcontrol.entity.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByScheduleId(UUID scheduleId);
    List<Task> findByScheduleIdOrderByStartTimeAsc(UUID scheduleId);
}
