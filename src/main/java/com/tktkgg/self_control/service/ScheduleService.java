package com.tktkgg.self_control.service;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import com.tktkgg.self_control.dao.ScheduleDao;
import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.util.SessionManager;

public class ScheduleService {
	private final ScheduleDao sd = new ScheduleDao();
	
	private String getDayOfTheWeekShort() { 
		LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        String jpDay = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
        
        return jpDay;
	}
	
	public List<Schedule> getTodaySchedule() throws ClassNotFoundException, SQLException {
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
