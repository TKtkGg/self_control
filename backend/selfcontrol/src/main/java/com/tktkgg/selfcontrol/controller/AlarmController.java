package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tktkgg.selfcontrol.dto.response.AlarmResponse;
import com.tktkgg.selfcontrol.service.AlarmService;

@RestController
@RequestMapping("/api/alarm") 
public class AlarmController {
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping("/next")
    public AlarmResponse getNextTask() {
        return alarmService.getNextTask();
    }
}
