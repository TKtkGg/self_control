package com.tktkgg.self_control.service;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import com.tktkgg.self_control.dao.ScheduleDao;
import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.util.SessionManager;

public class ScheduleService {
	private final ScheduleDao sd = new ScheduleDao();
	
	private DayOfWeek getDayOfTheWeekShort() { 
		LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        
        return dayOfWeek;
	}
	
	public Schedule getTodaySchedule() throws ClassNotFoundException, SQLException {
		return sd.findByUserIdAndDayOfWeek(SessionManager.getUser().getId(), getDayOfTheWeekShort());	
	}
	
	public Schedule getTheSchedule(int id) throws ClassNotFoundException, SQLException {
		return sd.findById(id);
	}
	
	public void createSchedule(Schedule schedule) throws ClassNotFoundException, SQLException {
		sd.create(schedule);
	}
	
	public void updateSchedule(Schedule schedule) throws ClassNotFoundException, SQLException {
		sd.update(schedule);
	}
	
	public void deleteSchedule(int id) throws ClassNotFoundException, SQLException {
		sd.delete(id);
	}
}
