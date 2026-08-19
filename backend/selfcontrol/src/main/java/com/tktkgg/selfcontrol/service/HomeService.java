package com.tktkgg.selfcontrol.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.repository.TaskRepository;
import com.tktkgg.selfcontrol.dto.response.HomeResponse;
import com.tktkgg.selfcontrol.entity.Schedule;
import com.tktkgg.selfcontrol.entity.Task;
import com.tktkgg.selfcontrol.dto.response.DayScheduleResponse;
import com.tktkgg.selfcontrol.dto.response.TaskResponse;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class HomeService {
    private final ScheduleRepository scheduleRepository;
    private final TaskRepository taskRepository;
    private final AuthService authService;

    public HomeService(ScheduleRepository scheduleRepository, TaskRepository taskRepository, AuthService authService) {
        this.scheduleRepository = scheduleRepository;
        this.taskRepository = taskRepository;
        this.authService = authService;
    }

    @Transactional
    public HomeResponse getHomeByCurrentUser() {
        UUID userId = authService.getCurrentUserId();
        List<Schedule> schedules = scheduleRepository.findByUserId(userId);
        if (schedules.isEmpty()) {
            return new HomeResponse(List.of());
        }

        List<DayScheduleResponse> dayScheduleResponses = new ArrayList<>();
        
        for (Schedule schedule : schedules) {
            List<Task> tasks = taskRepository.findByScheduleId(schedule.getId());
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

        return new HomeResponse(dayScheduleResponses);
    }
}
