package com.tktkgg.selfcontrol.service;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.repository.SettingRepository;
import com.tktkgg.selfcontrol.repository.TaskRepository;
import com.tktkgg.selfcontrol.dto.response.UserScheduleResponse;
import com.tktkgg.selfcontrol.entity.Schedule;
import com.tktkgg.selfcontrol.entity.Setting;
import com.tktkgg.selfcontrol.entity.Task;
import com.tktkgg.selfcontrol.dto.response.DayScheduleResponse;
import com.tktkgg.selfcontrol.dto.response.TaskResponse;

import java.util.UUID;
import java.util.List;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.tktkgg.selfcontrol.exception.ApiException;

@Service
public class UserScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TaskRepository taskRepository;
    private final SettingRepository settingRepository;
    private final AuthService authService;

    public UserScheduleService(
        ScheduleRepository scheduleRepository, 
        TaskRepository taskRepository,
        SettingRepository settingRepository,
        AuthService authService
    ) {
        this.scheduleRepository = scheduleRepository;
        this.taskRepository = taskRepository;
        this.settingRepository = settingRepository;
        this.authService = authService;
    }

    public UserScheduleResponse getUserSchedule(UUID userId) {
        Setting setting = settingRepository.findByUserId(userId).orElseThrow(() -> 
            ApiException.notFound("SETTING_NOT_FOUND", "Setting is missing")
        );

        if (!userId.equals(authService.getCurrentUserId()) && setting.getIsPublic() == false) {
            throw ApiException.forbidden("USER_FORBIDDEN", "CurrentUser is forbidden");
        }
        
        List<Schedule> schedules = scheduleRepository.findByUserIdOrderByDayOfWeekAsc(userId);
        if (schedules.isEmpty()) {
            return new UserScheduleResponse(List.of());
        }

        List<DayScheduleResponse> dayScheduleResponses = new ArrayList<>();
        
        for (Schedule schedule : schedules) {
            List<Task> tasks = taskRepository.findByScheduleIdOrderByStartTimeAsc(schedule.getId());
            int dayOfWeek = schedule.getDayOfWeek().ordinal();
            if (tasks.isEmpty()) {
                dayScheduleResponses.add(new DayScheduleResponse(dayOfWeek, schedule.getTitle(), List.of()));
                continue;
            }
            List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> new TaskResponse(task.getId(), task.getName(), task.getStartTime(), task.getEndTime()))
                .collect(Collectors.toList());
                
            dayScheduleResponses.add(new DayScheduleResponse(dayOfWeek, schedule.getTitle(), taskResponses));
        }

        return new UserScheduleResponse(dayScheduleResponses);
    }

    public DayScheduleResponse getUserSpecificSchedule(UUID userID, int dayOfWeek) {
        Schedule schedule = scheduleRepository.findByUserIdAndDayOfWeek(
            userID, toDayOfWeek(dayOfWeek)
        ).orElseThrow(() -> 
            ApiException.notFound("SCHEDULE_NOT_FOUND", "Schedule not found.")
        );

        List<Task> tasks = taskRepository.findByScheduleIdOrderByStartTimeAsc(schedule.getId());

        List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> new TaskResponse(task.getId(), task.getName(), task.getStartTime(), task.getEndTime()))
                .collect(Collectors.toList());

        return new DayScheduleResponse(
            dayOfWeek,
            schedule.getTitle(),
            taskResponses
        );
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
