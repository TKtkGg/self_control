package com.tktkgg.selfcontrol.service;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.entity.Task;
import com.tktkgg.selfcontrol.repository.TaskRepository;
import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.entity.Schedule;

@Service
public class TaskService  {
    private final TaskRepository taskRepository;
    private final ScheduleRepository scheduleRepository;
    private final AuthService authService;

    public TaskService(TaskRepository taskRepository, ScheduleRepository scheduleRepository, AuthService authService) {
        this.taskRepository = taskRepository;
        this.scheduleRepository = scheduleRepository;
        this.authService = authService;
    }

    private boolean isValidTime(LocalTime startTime, LocalTime endTime) {
        return startTime.isBefore(endTime);
    }

    public void createTask(int dayOfWeek, int startHour, int startMinute, int endHour, int endMinute, String name) {
        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);

        if (!isValidTime(startTime, endTime)) {
            throw new IllegalArgumentException("Invalid time");
        }

        Task task = new Task();

        Schedule schedule = 
            scheduleRepository.findByUserIdAndDayOfWeek(
                authService.getCurrentUserId(), DayOfWeek.values()[dayOfWeek]
            ).orElseThrow(() -> 
                new IllegalArgumentException("Schedule not found")
            );

        task.setSchedule(schedule);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setName(name);
        taskRepository.save(task);
    }

    public void updateTask(UUID taskId, int startHour, int startMinute, int endHour, int endMinute, String name) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> 
            new IllegalArgumentException("Task not found")
        );

        if (!task.getSchedule().getUser().getId().equals(authService.getCurrentUserId())) {
            throw new IllegalArgumentException("Unauthorized");
        }

        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);

        if (!isValidTime(startTime, endTime)) {
            throw new IllegalArgumentException("Invalid time");
        }

        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setName(name);
        taskRepository.save(task);
    }

    public void deleteTask(UUID taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> 
            new IllegalArgumentException("Task not found")
        );

        if (!task.getSchedule().getUser().getId().equals(authService.getCurrentUserId())) {
            throw new IllegalArgumentException("Unauthorized");
        }

        taskRepository.delete(task);
    }

}
