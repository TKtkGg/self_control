package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import com.tktkgg.selfcontrol.service.TaskService;
import com.tktkgg.selfcontrol.service.ScheduleService;
import com.tktkgg.selfcontrol.service.HomeService;
import com.tktkgg.selfcontrol.dto.response.HomeResponse;
import com.tktkgg.selfcontrol.dto.request.TaskRequest;
import com.tktkgg.selfcontrol.dto.request.UpdateTaskRequest;

@RestController
public class HomeController {
    private final TaskService taskService;
    private final ScheduleService scheduleService;
    private final HomeService homeService;

    public HomeController(TaskService taskService, ScheduleService scheduleService, HomeService homeService) {
        this.taskService = taskService;
        this.scheduleService = scheduleService;
        this.homeService = homeService;
    }

    @GetMapping("/api/home")
    public HomeResponse home() {
        return homeService.getHomeByCurrentUser();
    }

    @PatchMapping("/api/home/schedule/{dayOfWeek}")
    public ResponseEntity<Map<String, String>> updateScheduleTitle(
        @PathVariable int dayOfWeek,
        @RequestBody Map<String, String> request
    ) {
        scheduleService.updateTitle(dayOfWeek, request.get("title"));
        return ResponseEntity.ok(Map.of("message", "Schedule updated successfully"));
    }

    @PostMapping("/api/home/task")
    public ResponseEntity<Map<String, String>> createTask(@RequestBody TaskRequest request) {
        taskService.createTask(
            request.getDayOfWeek(), 
            request.getStartHour(),
            request.getStartMinute(), 
            request.getEndHour(), 
            request.getEndMinute(), 
            request.getName()
        );

        return ResponseEntity.ok(Map.of("message", "Task created successfully"));
    }
    
    @PatchMapping("/api/home/task/{taskId}")
    public ResponseEntity<Map<String, String>> updateTask(@PathVariable UUID taskId, @RequestBody UpdateTaskRequest request) {
        taskService.updateTask(
            taskId, 
            request.getStartHour(),
            request.getStartMinute(), 
            request.getEndHour(),
            request.getEndMinute(), 
            request.getName()
        );

        return ResponseEntity.ok(Map.of("message", "Task updated successfully"));
    }

    @DeleteMapping("/api/home/task/{taskId}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable UUID taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
    }
}
