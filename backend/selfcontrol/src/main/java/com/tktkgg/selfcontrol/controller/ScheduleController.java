package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import com.tktkgg.selfcontrol.service.AuthService;
import com.tktkgg.selfcontrol.service.ScheduleService;
import com.tktkgg.selfcontrol.service.UserScheduleService;
import com.tktkgg.selfcontrol.dto.response.DayScheduleResponse;
import com.tktkgg.selfcontrol.dto.response.UserScheduleResponse;
import com.tktkgg.selfcontrol.dto.request.UpdateScheduleRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final AuthService authService;
    private final ScheduleService scheduleService;
    private final UserScheduleService userScheduleService;

    public ScheduleController(AuthService authService, ScheduleService scheduleService, UserScheduleService userScheduleService) {
        this.authService = authService;
        this.scheduleService = scheduleService;
        this.userScheduleService = userScheduleService;
    }

    @GetMapping("")
    public UserScheduleResponse getSchedules() {
        return userScheduleService.getUserSchedule(authService.getCurrentUserId());
    }

    @GetMapping("/{dayOfWeek}")
    public DayScheduleResponse getSpecificSchedule(
        @PathVariable @Min(0) @Max(6) int dayOfWeek
    ) {
        return userScheduleService.getUserSpecificSchedule(authService.getCurrentUserId(), dayOfWeek);
    }

    @PatchMapping("/{dayOfWeek}")
    public ResponseEntity<Map<String, String>> updateScheduleTitle(
        @PathVariable @Min(0) @Max(6) int dayOfWeek,
        @Valid @RequestBody UpdateScheduleRequest request
    ) {
        scheduleService.updateTitle(dayOfWeek, request.title());
        return ResponseEntity.ok(Map.of("message", "Schedule updated successfully"));
    }
}
