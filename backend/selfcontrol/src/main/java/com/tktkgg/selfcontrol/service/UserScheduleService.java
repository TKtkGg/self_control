package com.tktkgg.selfcontrol.service;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.repository.TaskRepository;
import com.tktkgg.selfcontrol.dto.response.UserScheduleResponse;
import com.tktkgg.selfcontrol.entity.Schedule;
import com.tktkgg.selfcontrol.entity.Task;
import com.tktkgg.selfcontrol.dto.response.DayScheduleResponse;
import com.tktkgg.selfcontrol.dto.response.TaskResponse;

import java.util.UUID;
import java.util.List;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class UserScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TaskRepository taskRepository;

    public UserScheduleService(ScheduleRepository scheduleRepository, TaskRepository taskRepository) {
        this.scheduleRepository = scheduleRepository;
        this.taskRepository = taskRepository;
    }

    public UserScheduleResponse getUserSchedule(UUID userId) {
        List<Schedule> schedules = scheduleRepository.findByUserIdOrderByDayOfWeekAsc(userId);
        if (schedules.isEmpty()) {
            return new UserScheduleResponse(List.of());
        }

        List<DayScheduleResponse> dayScheduleResponses = new ArrayList<>();
        
        for (Schedule schedule : schedules) {
            List<Task> tasks = taskRepository.findByScheduleIdOrderByStartTimeAsc(schedule.getId());
            int dayOfWeek = schedule.getDayOfWeek().ordinal();
            if (tasks.isEmpty()) {
                dayScheduleResponses.add(new DayScheduleResponse(dayOfWeek, schedule.getTitle(), List.of()));
                continue;
            }
            List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> new TaskResponse(task.getId(), task.getName(), task.getStartTime(), task.getEndTime()))
                .collect(Collectors.toList());
                
            dayScheduleResponses.add(new DayScheduleResponse(dayOfWeek, schedule.getTitle(), taskResponses));
        }

        return new UserScheduleResponse(dayScheduleResponses);
    }

    public DayScheduleResponse getUserSpecificSchedule(UUID userID, int dayOfWeek) {
        Schedule schedule = scheduleRepository.findByUserIdAndDayOfWeek(
            userID, DayOfWeek.values()[dayOfWeek]
        ).orElseThrow(() -> 
            new IllegalArgumentException("Schedule not found")
        );

        List<Task> tasks = taskRepository.findByScheduleIdOrderByStartTimeAsc(schedule.getId());

        List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> new TaskResponse(task.getId(), task.getName(), task.getStartTime(), task.getEndTime()))
                .collect(Collectors.toList());

        return new DayScheduleResponse(
            dayOfWeek,
            schedule.getTitle(),
            taskResponses
        );
    }
}
