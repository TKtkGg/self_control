package com.tktkgg.self_control.service;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import com.tktkgg.self_control.dao.ScheduleDao;
import com.tktkgg.self_control.dao.TaskDao;
import com.tktkgg.self_control.exception.DatabaseException;
import com.tktkgg.self_control.exception.InvalidTimeException;
import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.SessionManager;

public class ScheduleService {
	private final ScheduleDao sd = new ScheduleDao();
	private final TaskDao td = new TaskDao();
	
	private DayOfWeek getDayOfTheWeekShort() { 
		LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        
        return dayOfWeek;
	}
	
	public Schedule getTodaySchedule(){
		try {
			return sd.findByUserIdAndDayOfWeek(SessionManager.getUser().getId(), getDayOfTheWeekShort());
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public Schedule getSpecificSchedule(DayOfWeek day) {
		try {
			return sd.findByUserIdAndDayOfWeek(SessionManager.getUser().getId(), day);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public Schedule getSpecificSchedule(User user, DayOfWeek day) {
		try {
			return sd.findByUserIdAndDayOfWeek(user.getId(), day);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public Schedule getTheSchedule(int id) {
		try {
			return sd.findById(id);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public Schedule createSchedule(Schedule schedule) {
		try {
			return sd.create(schedule);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void createSchedule(Schedule schedule, Task task) throws InvalidTimeException {
		if (!task.isTimeValid()) {
			throw new InvalidTimeException("無効な時間です");
		}
		
		try {
			Schedule newSchedule = sd.create(schedule);
			task.setScheduleId(newSchedule.getId());
			td.create(task);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
	}
	
	public void updateSchedule(Schedule schedule) {
		try {
			sd.update(schedule);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void updateSchedule(Schedule schedule, Task task) throws InvalidTimeException {
		if (!task.isTimeValid()) {
			throw new InvalidTimeException("無効な時間です");
		}
		
		try {
			sd.update(schedule);
			td.update(task);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void deleteSchedule(int id) {
		try {
			sd.delete(id);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
}
