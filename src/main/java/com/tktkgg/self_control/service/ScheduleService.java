package com.tktkgg.self_control.service;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import com.tktkgg.self_control.dao.ScheduleDao;
import com.tktkgg.self_control.dao.TaskDao;
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
	
	public Schedule getTodaySchedule() throws ClassNotFoundException, SQLException {
		return sd.findByUserIdAndDayOfWeek(SessionManager.getUser().getId(), getDayOfTheWeekShort());	
	}
	
	public Schedule getSpecificSchedule(DayOfWeek day) throws ClassNotFoundException, SQLException {
		return sd.findByUserIdAndDayOfWeek(SessionManager.getUser().getId(), day);
	}
	
	public Schedule getSpecificSchedule(User user, DayOfWeek day) throws ClassNotFoundException, SQLException {
		return sd.findByUserIdAndDayOfWeek(user.getId(), day);
	}
	
	public Schedule getTheSchedule(int id) throws ClassNotFoundException, SQLException {
		return sd.findById(id);
	}
	
	public Schedule createSchedule(Schedule schedule) throws ClassNotFoundException, SQLException {
		return sd.create(schedule);
	}
	
	public void createSchedule(Schedule schedule, Task task) throws ClassNotFoundException, SQLException, InvalidTimeException {
		if (!task.isTimeValid()) {
			throw new InvalidTimeException("無効な時間です");
		}
		
		Schedule newSchedule = sd.create(schedule);
		task.setScheduleId(newSchedule.getId());
		td.create(task);
	}
	
	public void updateSchedule(Schedule schedule) throws ClassNotFoundException, SQLException {
		sd.update(schedule);
	}
	
	public void updateSchedule(Schedule schedule, Task task) throws ClassNotFoundException, SQLException, InvalidTimeException {
		if (!task.isTimeValid()) {
			throw new InvalidTimeException("無効な時間です");
		}
		sd.update(schedule);
		td.update(task);
	}
	
	public void deleteSchedule(int id) throws ClassNotFoundException, SQLException {
		sd.delete(id);
	}
}
