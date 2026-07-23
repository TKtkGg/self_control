package com.tktkgg.self_control.view.schedule;

import java.time.DayOfWeek;
import java.util.List;

import com.tktkgg.self_control.exception.InvalidTimeException;
import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.service.ScheduleService;
import com.tktkgg.self_control.service.TaskService;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.util.InputUtils;
import com.tktkgg.self_control.util.SessionManager;
import com.tktkgg.self_control.view.MenuAction;
import com.tktkgg.self_control.view.ViewUtils;

public class EditScheduleView implements MenuAction {
	private final ScheduleService ss = new ScheduleService();
	private final TaskService ts = new TaskService();
	private final ScheduleInputView siv = new ScheduleInputView();
	
	private boolean confirm(String action) {
		while (true) {
	        int input = Input.nextInt();

	        if (input == 1) {
	        	System.out.println(action + "しました");
	        	return true;
	        }
	        if (input == 2) {
	        	System.out.println(action + "しませんでした");
	        	return false;
	        }

	        System.out.println("1か2を入力してください。");
	    }
	}
	
	@Override
	public void execute() {
		System.out.println("何曜日を編集しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）（0で戻る）");
		int i = InputUtils.inputWeek();
		if (i == 0) return;
		
		DayOfWeek day = DayOfWeek.of(i);
		
		Schedule schedule = ss.getSpecificSchedule(day);
		if (schedule == null) {
			System.out.println("スケジュールが存在していません");
			return;
		}
		
		List<Task> tasks = ts.getTasks(schedule.getId());
		if (tasks.isEmpty()) {
			System.out.println("タスクがありません");
			return;
		}
		
		String title = siv.inputTitle(schedule.getTitle());
		
		ViewUtils.viewTaskWithId(tasks);
		System.out.println("どのタスクを編集しますか？（番号を入力）");
		
		Task task = null;
		while (true) {
			int id = Input.nextInt();
			
			task = ts.getTask(id);
			if (task == null || !task.belongsTo(schedule)) {
				System.out.println("存在しないIDです。");
				continue;
			} else {
				break;
			}
		}
		
		Task newTask = siv.scheduleInputView(schedule, title, task);
		Schedule newSchedule = new Schedule(schedule.getId(), SessionManager.getUser().getId(), day, title);
		
		if (confirm("更新")) {
			newTask.setId(task.getId());
			newTask.setScheduleId(newSchedule.getId());
			try {
				ss.updateSchedule(newSchedule, newTask);
			} catch (InvalidTimeException e) {
				System.out.println(e.getMessage());
			}
			
		}
	}
}
