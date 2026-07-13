package com.tktkgg.self_control.view;

import java.sql.SQLException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.List;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.service.ScheduleService;
import com.tktkgg.self_control.service.TaskService;

public class ScheduleView {
	ScheduleService ss = new ScheduleService();
	TaskService ts = new TaskService();
	public void checkScheduleView() throws ClassNotFoundException, SQLException {
		Schedule schedule = ss.getTodaySchedule();
		List<Task> tasks = ts.getTasks(schedule.getId());
		
		System.out.println();
		
		System.out.println(schedule.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.JAPANESE) + "のスケジュール");
		System.out.println("タイトル：" + schedule.getTitle());
		for (Task task : tasks) {
			System.out.println(task.getTaskName());
			System.out.println(task.getStartTime() + " ~ " + task.getEndTime());
			System.out.println(task.getMemo());
		}
		
		System.out.println();
	}
	
	public void editScheduleView() {
		
	}
	
	public void addScheduleView() {
		
	}
}
