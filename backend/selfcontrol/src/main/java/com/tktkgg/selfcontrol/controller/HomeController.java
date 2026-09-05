package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.UUID;

import com.tktkgg.selfcontrol.service.AuthService;
import com.tktkgg.selfcontrol.service.TaskService;
import com.tktkgg.selfcontrol.service.ScheduleService;
import com.tktkgg.selfcontrol.service.UserScheduleService;
import com.tktkgg.selfcontrol.dto.response.UserScheduleResponse;
import com.tktkgg.selfcontrol.dto.request.TaskRequest;
import com.tktkgg.selfcontrol.dto.request.UpdateTaskRequest;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final AuthService authService;
    private final TaskService taskService;
    private final ScheduleService scheduleService;
    private final UserScheduleService userScheduleService;

    public HomeController(AuthService authService, TaskService taskService, ScheduleService scheduleService, UserScheduleService userScheduleService) {
        this.authService = authService;
        this.taskService = taskService;
        this.scheduleService = scheduleService;
        this.userScheduleService = userScheduleService;
    }

    @GetMapping("")
    public UserScheduleResponse home() {
        return userScheduleService.getUserSchedule(authService.getCurrentUserId());
    }

    @PatchMapping("/schedule/{dayOfWeek}")
    public ResponseEntity<Map<String, String>> updateScheduleTitle(
        @PathVariable int dayOfWeek,
        @RequestBody Map<String, String> request
    ) {
        scheduleService.updateTitle(dayOfWeek, request.get("title"));
        return ResponseEntity.ok(Map.of("message", "Schedule updated successfully"));
    }

    @PostMapping("/task")
    public ResponseEntity<Map<String, String>> createTask(@RequestBody TaskRequest request) {
        taskService.createTask(
            request.dayOfWeek(), 
            request.startHour(),
            request.startMinute(), 
            request.endHour(), 
            request.endMinute(), 
            request.name()
        );

        return ResponseEntity.ok(Map.of("message", "Task created successfully"));
    }
    
    @PatchMapping("/task/{taskId}")
    public ResponseEntity<Map<String, String>> updateTask(@PathVariable UUID taskId, @RequestBody UpdateTaskRequest request) {
        taskService.updateTask(
            taskId, 
            request.startHour(),
            request.startMinute(), 
            request.endHour(),
            request.endMinute(), 
            request.name()
        );

        return ResponseEntity.ok(Map.of("message", "Task updated successfully"));
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable UUID taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
    }
}
