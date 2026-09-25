package com.tktkgg.selfcontrol.service;

import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.entity.Schedule;

import java.time.DayOfWeek;

import org.springframework.stereotype.Service;
import com.tktkgg.selfcontrol.exception.ApiException;

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
            authService.getCurrentUserId(), toDayOfWeek(dayOfWeek)
        ).orElseThrow(() -> 
            ApiException.notFound("SCHEDULE_NOT_FOUND", "Schedule not found.")
        );

        if (title == null || title.length() > 30) {
            throw ApiException.badRequest(
                "INVALID_SCHEDULE_TITLE",
                "Title must be 30 characters or fewer."
            );
        }

        schedule.setTitle(title);
        scheduleRepository.save(schedule);
    }

    private DayOfWeek toDayOfWeek(int dayOfWeek) {
        if (dayOfWeek < 0 || dayOfWeek > 6) {
            throw ApiException.badRequest(
                "INVALID_DAY_OF_WEEK",
                "dayOfWeek must be between 0 and 6."
            );
        }
        return DayOfWeek.values()[dayOfWeek];
    }
}
