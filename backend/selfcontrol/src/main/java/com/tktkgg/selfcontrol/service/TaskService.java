package com.tktkgg.selfcontrol.service;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
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

    private boolean isValidTime(
        LocalTime startTime,
        LocalTime endTime,
        UUID scheduleId,
        UUID excludeTaskId
    ) {
        if (!startTime.isBefore(endTime)) {
            return false;
        }
    
        List<Task> tasks = taskRepository.findByScheduleId(scheduleId);
    
        return tasks.stream()
            // 編集時は自分自身を比較対象から除外する
            .filter(task ->
                excludeTaskId == null || !excludeTaskId.equals(task.getId())
            )
            .noneMatch(task ->
                startTime.isBefore(task.getEndTime())
                    && task.getStartTime().isBefore(endTime)
            );
    }

    public void createTask(int dayOfWeek, int startHour, int startMinute, int endHour, int endMinute, String name) {
        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);

        Task task = new Task();

        Schedule schedule = 
            scheduleRepository.findByUserIdAndDayOfWeek(
                authService.getCurrentUserId(), DayOfWeek.values()[dayOfWeek]
            ).orElseThrow(() -> 
                new IllegalArgumentException("Schedule not found")
            );
        
        if (!isValidTime(startTime, endTime, schedule.getId(), null)) {
            throw new IllegalArgumentException("Invalid time");
        }

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

        if (!isValidTime(startTime, endTime, task.getSchedule().getId(), task.getId())) {
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
