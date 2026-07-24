package com.tktkgg.self_control.view.schedule;

import java.time.DayOfWeek;

import com.tktkgg.self_control.exception.InvalidTimeException;
import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.service.ScheduleService;
import com.tktkgg.self_control.service.TaskService;
import com.tktkgg.self_control.util.InputUtils;
import com.tktkgg.self_control.util.SessionManager;
import com.tktkgg.self_control.view.MenuAction;
import com.tktkgg.self_control.view.ViewUtils;

public class AddScheduleView implements MenuAction {
	private final ScheduleService ss = new ScheduleService();
	private final TaskService ts = new TaskService();
	private final ScheduleInputView siv = new ScheduleInputView();
	
	@Override
	public void execute() {
		System.out.println("何曜日に追加しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）（0で戻る）");
		int i = InputUtils.inputWeek();
		if (i == 0) return;
		
		DayOfWeek day = DayOfWeek.of(i);
		Schedule schedule = ss.getSpecificSchedule(day);
		String title = null;
		if (schedule == null) {
			title = siv.inputTitle("");
		} else {
			title = schedule.getTitle();
		}
		
		Task newTask = siv.scheduleInputView(schedule, title, null);
		Schedule newSchedule = new Schedule(0, SessionManager.getUser().getId(), day, title);
		
		if (ViewUtils.confirm("作成")) {
			try {
				if (schedule == null) {
					ss.createSchedule(newSchedule, newTask);
				} else {
					newTask.setScheduleId(schedule.getId());
					ts.createTask(newTask);
				}	
			} catch (InvalidTimeException e) {
				System.out.println(e.getMessage());
			}	
		} else {
			newTask.setScheduleId(schedule.getId());
			ts.createTask(newTask);
		}
	}
}
