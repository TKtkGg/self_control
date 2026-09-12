package com.tktkgg.selfcontrol.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.dto.response.AlarmResponse;
import com.tktkgg.selfcontrol.dto.response.TaskResponse;
import com.tktkgg.selfcontrol.entity.Schedule;
import com.tktkgg.selfcontrol.entity.Task;
import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.repository.TaskRepository;

@Service 
public class AlarmService {
    private final AuthService authService;
    private final ScheduleRepository scheduleRepository;
    private final TaskRepository taskRepository;

    public AlarmService(
        AuthService authService, 
        ScheduleRepository scheduleRepository,
        TaskRepository taskRepository
    ) {
        this.authService = authService;
        this.scheduleRepository = scheduleRepository;
        this.taskRepository = taskRepository;
    }

    private AlarmResponse convertAlarmResponse(Task task, LocalDateTime nextStartAt) {
        TaskResponse taskResponse = new TaskResponse(
            task.getId(),
            task.getName(),
            task.getStartTime(),
            task.getEndTime()
        );

        return new AlarmResponse(
            taskResponse,
            nextStartAt
        );
    }

    public AlarmResponse getNextTask() {
        LocalDateTime today = LocalDateTime.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        for (int i = 0; i < 7; i++) {
            DayOfWeek targetDay = dayOfWeek.plus(i);

            Optional<Schedule> scheduleOptional = scheduleRepository.findByUserIdAndDayOfWeek(
                authService.getCurrentUserId(), targetDay
            );

            if (scheduleOptional.isEmpty()) continue;

            Schedule schedule = scheduleOptional.get();
    
            List<Task> tasks = taskRepository.findByScheduleIdOrderByStartTimeAsc(schedule.getId());
    
            if (i == 0) {
                for (Task task : tasks) {
                    if (task.getStartTime().isAfter(today.toLocalTime())) {
                        return convertAlarmResponse(task, today.with(task.getStartTime()));
                    }
                }
            } else {
                if (!tasks.isEmpty()) {
                    Task task = tasks.getFirst();
                    LocalDateTime nextStartAt = today.plusDays(i);
                    return convertAlarmResponse(task, nextStartAt.with(task.getStartTime()));
                } 
            }
        }
        return new AlarmResponse(
            null,
            null
        );
    }
}
