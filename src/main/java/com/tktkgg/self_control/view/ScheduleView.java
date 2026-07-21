package com.tktkgg.self_control.view;

import java.sql.SQLException;
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

public class ScheduleView {
	private final ScheduleService ss = new ScheduleService();
	private final TaskService ts = new TaskService();
	private final ScheduleInputView sv = new ScheduleInputView();
	
	private boolean confirm() {
		while (true) {
	        int input = Input.nextInt();

	        if (input == 1) return true;
	        if (input == 2) return false;

	        System.out.println("1か2を入力してください。");
	    }
	}
	
	public void checkScheduleView(boolean isToday) throws ClassNotFoundException, SQLException {
		Schedule schedule = null;
		List<Task> tasks = null;
		if (isToday) {
			schedule = ss.getTodaySchedule();
			if (schedule == null) {
				System.out.println("スケジュールが存在していません");
				return;
			}
			
			tasks = ts.getTasks(schedule.getId());
		} else {
			System.out.println("何曜日のスケジュールを確認しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）（0で戻る）");
			int i = InputUtils.inputWeek();
			if (i == 0) return;
			
			DayOfWeek day = DayOfWeek.of(i);
			
			schedule = ss.getSpecificSchedule(day);
			if (schedule == null) {
				System.out.println("スケジュールが存在していません");
				return;
			}
			
			tasks = ts.getTasks(schedule.getId());
		}
		
		System.out.println();
		
		System.out.println(schedule.getDisplayDay() + "のスケジュール");
		System.out.println("タイトル：" + schedule.getTitle());
		ViewUtils.viewTask(tasks);
		
		System.out.println();
	}
	
	public void editScheduleView() throws ClassNotFoundException, SQLException, InvalidTimeException {
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
		
		System.out.println("スケジュールタイトル：" + schedule.getTitle());
		System.out.print("(入力)→");
		String title = Input.nextLine();
		
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
		
		Task newTask = sv.scheduleInputView(schedule, title, task);
		Schedule newSchedule = new Schedule(schedule.getId(), SessionManager.getUser().getId(), day, title);
		
		if (confirm()) {
			newTask.setId(task.getId());
			newTask.setScheduleId(newSchedule.getId());
			ss.updateSchedule(newSchedule, newTask);
			System.out.println("更新しました");
		} else {
			System.out.println("更新しませんでした");
		}
		
	}
	
	public void addScheduleView() throws ClassNotFoundException, SQLException, InvalidTimeException {
		System.out.println("何曜日に追加しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）（0で戻る）");
		int i = InputUtils.inputWeek();
		if (i == 0) return;
		
		DayOfWeek day = DayOfWeek.of(i);
		Schedule schedule = ss.getSpecificSchedule(day);
		String title = null;
		if (schedule == null) {
			System.out.println("スケジュールタイトル");
			System.out.print("(入力)→");
			title = Input.nextLine();
		}
		
		Task newTask = sv.scheduleInputView(schedule, title, null);
		Schedule newSchedule = new Schedule(0, SessionManager.getUser().getId(), day, title);
		
		if (confirm()) {
			if (schedule == null) {
				ss.createSchedule(newSchedule, newTask);
			} else {
				newTask.setScheduleId(schedule.getId());
				ts.createTask(newTask);
			}
			System.out.println("作成しました");
		} else {
			System.out.println("作成しませんでした");
		}
	}
}
