package com.tktkgg.self_control.view.schedule;

import java.time.DayOfWeek;
import java.util.List;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.service.ScheduleService;
import com.tktkgg.self_control.service.TaskService;
import com.tktkgg.self_control.util.InputUtils;
import com.tktkgg.self_control.view.MenuAction;
import com.tktkgg.self_control.view.ViewUtils;

public class CheckScheduleView implements MenuAction {
	private final ScheduleService ss = new ScheduleService();
	private final TaskService ts = new TaskService();
	private boolean isToday;
	
	public CheckScheduleView(boolean isToday) {
		this.isToday = isToday;
	}
	
	private Schedule selectSchedule(boolean isToday) {
		Schedule schedule = null;
		if (isToday) {
			schedule = ss.getTodaySchedule();
		} else {
			System.out.println("何曜日のスケジュールを確認しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）（0で戻る）");
			int i = InputUtils.inputWeek();
			if (i == 0) return null;
			
			DayOfWeek day = DayOfWeek.of(i);
			
			schedule = ss.getSpecificSchedule(day);
		}
		
		if (schedule == null) {
			System.out.println("スケジュールが存在しません");
		}
		return schedule;
	}
	
	public void execute() {
		Schedule schedule = null;
		List<Task> tasks = null;
		
		schedule = selectSchedule(isToday);
		if (schedule == null) {
			return;
		}
		
		tasks = ts.getTasks(schedule.getId());
		
		System.out.println();
		
		ViewUtils.viewTitle(schedule, tasks);
		
		System.out.println();
	}
}
