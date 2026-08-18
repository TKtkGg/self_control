package com.tktkgg.selfcontrol.service;

import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.entity.Schedule;

import java.time.DayOfWeek;

import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AuthService authService;

    public ScheduleService(ScheduleRepository scheduleRepository, AuthService authService) {
        this.scheduleRepository = scheduleRepository;
        this.authService = authService;
    }

    public void updateTitle(int dayOfWeek, String title) {
        Schedule schedule = scheduleRepository.findByUserIdAndDayOfWeek(
            authService.getCurrentUserId(), DayOfWeek.values()[dayOfWeek]
        ).orElseThrow(() -> 
            new IllegalArgumentException("Schedule not found")
        );

        if (title.length() > 30) {
            throw new IllegalArgumentException("Title must be less than 30 characters");
        }

        schedule.setTitle(title);
        scheduleRepository.save(schedule);
    }
}
