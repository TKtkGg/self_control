package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.UUID;

import com.tktkgg.selfcontrol.service.TaskService;
import com.tktkgg.selfcontrol.dto.request.TaskRequest;
import com.tktkgg.selfcontrol.dto.request.UpdateTaskRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> createTask(@Valid @RequestBody TaskRequest request) {
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
    
    @PatchMapping("/{taskId}")
    public ResponseEntity<Map<String, String>> updateTask(@PathVariable UUID taskId, @Valid @RequestBody UpdateTaskRequest request) {
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

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable UUID taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
    }
}
