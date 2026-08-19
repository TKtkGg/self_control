package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import com.tktkgg.selfcontrol.service.TaskService;
import com.tktkgg.selfcontrol.service.ScheduleService;
import com.tktkgg.selfcontrol.service.HomeService;
import com.tktkgg.selfcontrol.dto.response.HomeResponse;

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
}
