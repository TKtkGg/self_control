package com.tktkgg.self_control.view;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.service.ScheduleService;
import com.tktkgg.self_control.service.TaskService;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.util.InputUtils;
import com.tktkgg.self_control.util.SessionManager;

public class ScheduleView {
	ScheduleService ss = new ScheduleService();
	TaskService ts = new TaskService();

	private boolean isScheduleNull(Schedule schedule) {
		if (schedule == null) {
			System.out.println("スケジュールが存在していません。");
			return true;
		}
		return false;
	}
	
	private boolean checkTimeReverse(LocalTime startTime, LocalTime endTime) {
		boolean isReverse = false;
		if (!startTime.isBefore(endTime)) {
			System.out.println("開始時間が終了時間より遅い時間にありますが、本当によろしいですか？（1:はい 2:いいえ");
			while (true) {
			    int choice = Input.nextInt();
			    if (choice == 1) {
			    	isReverse = true;
			        break;
			    } else if (choice == 2) {
			        System.out.println("作成しませんでした。");
			        isReverse = false;
			        break;
			    }
			    System.out.println("1か2を入力してください。");
			}
		}
		return isReverse;
	}

	
	public void checkScheduleView(boolean isToday) throws ClassNotFoundException, SQLException {
		Schedule schedule = null;
		List<Task> tasks = null;
		if (isToday) {
			schedule = ss.getTodaySchedule();
			if (isScheduleNull(schedule)) return;
			
			tasks = ts.getTasks(schedule.getId());
		} else {
			System.out.println("何曜日のスケジュールを確認しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）");
			int i = InputUtils.inputWeek();
			
			DayOfWeek day = DayOfWeek.of(i);
			
			schedule = ss.getSpecificSchedule(day);
			if (isScheduleNull(schedule)) return;
			
			tasks = ts.getTasks(schedule.getId());
		}
		
		System.out.println();
		
		System.out.println(schedule.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.JAPANESE) + "のスケジュール");
		System.out.println("タイトル：" + schedule.getTitle());
		for (Task task : tasks) {
			System.out.println(task.getTaskName());
			System.out.println(task.getStartTime() + " ~ " + task.getEndTime());
			System.out.println(task.getMemo());
			System.out.println();
		}
		
		System.out.println();
	}
	
	public void editScheduleView() throws ClassNotFoundException, SQLException {
		System.out.println("何曜日を編集しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）");
		int i = InputUtils.inputWeek();
		DayOfWeek day = DayOfWeek.of(i);
		
		Schedule schedule = ss.getSpecificSchedule(day);
		if (isScheduleNull(schedule)) return;
		
		List<Task> tasks = ts.getTasks(schedule.getId());
		if (tasks.isEmpty()) {
			System.out.println("タスクがありません");
			return;
		}
		
		System.out.println("スケジュールタイトル：" + schedule.getTitle());
		System.out.print("(入力)→");
		String title = Input.nextLine();
		
		for (Task task : tasks) {
			System.out.println(task.getId());
			System.out.println(task.getTaskName());
			System.out.println(task.getStartTime() + " ~ " + task.getEndTime());
		}
		System.out.println("どのタスクを編集しますか？（番号を入力）");
		
		Task task = null;
		while (true) {
			int id = Input.nextInt();
			
			task = ts.getTask(id);
			if (task == null || task.getScheduleId() != schedule.getId()) {
				System.out.println("存在しないIDです。");
				continue;
			} else {
				break;
			}
		}
		
		System.out.println("名前：" + task.getTaskName());
		System.out.print("(入力)→");
		String name = Input.nextLine();
		
		System.out.println("開始時間(時間/h)：" + task.getStartTime());
		System.out.print("(入力)→");
		int startTimeH = InputUtils.inputHour();
		
		System.out.println("開始時間(分/m)：" + task.getStartTime());
		System.out.print("(入力)→");
		int startTimeM = InputUtils.inputMinute();
		
		LocalTime startTime = LocalTime.of(startTimeH, startTimeM);
		
		System.out.println("終了時間（時間/h）：" + task.getEndTime());
		System.out.print("(入力)→");
		int endTimeH = InputUtils.inputHour();
		
		System.out.println("終了時間（分/m）：" + task.getEndTime());
		System.out.print("(入力)→");
		int endTimeM = InputUtils.inputMinute();
		
		LocalTime endTime = LocalTime.of(endTimeH, endTimeM);
		
		System.out.println("メモ：" + task.getMemo());
		System.out.print("(入力)→");
		String memo = Input.nextLine();
		
		System.out.println("この変更でよろしいですか？（1:はい 2:いいえ）");
		System.out.println("タイトル：" + title);
		System.out.println("名前：" + name);
		System.out.println(startTime + " ~ " + endTime);
		System.out.println("メモ：" + memo);
		
		while (true) {
			int isComplete = Input.nextInt();
			if (isComplete == 1) {
				if(checkTimeReverse(startTime, endTime)) return;
				
				Schedule newSchedule = new Schedule(schedule.getId(), SessionManager.getUser().getId(), day, title);
				Task newTask = new Task(task.getId(), schedule.getId(), startTime, endTime, name, memo);
				ss.updateSchedule(newSchedule);
				ts.updateTask(newTask);
				
				System.out.println("更新が完了しました。");
				break;
			} else if (isComplete == 2) {
				System.out.println("更新しませんでした。");
				break;
			} else {
				System.out.println("1か2を入力してください。");
				continue;
			}
		}
	}
	
	public void addScheduleView() throws ClassNotFoundException, SQLException {
		System.out.println("何曜日に追加しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）");
		int i = InputUtils.inputWeek();
		DayOfWeek day = DayOfWeek.of(i);
		Schedule schedule = ss.getSpecificSchedule(day);
		String title = null;
		if (schedule == null) {
			System.out.print("スケジュールタイトル：");
			title = Input.nextLine();
		}
		
		System.out.print("名前：");
		String name = Input.nextLine();
		
		System.out.print("開始時間(時間/h)：");
		int startTimeH = InputUtils.inputHour();
		
		System.out.print("開始時間(分/m)：");
		int startTimeM = InputUtils.inputMinute();
		
		LocalTime startTime = LocalTime.of(startTimeH, startTimeM);
		
		System.out.print("終了時間（時間/h）：");
		int endTimeH = InputUtils.inputHour();
		
		System.out.print("終了時間（分/m）：");
		int endTimeM = InputUtils.inputMinute();
		
		LocalTime endTime = LocalTime.of(endTimeH, endTimeM);
		
		System.out.print("メモ：");
		String memo = Input.nextLine();
		
		System.out.println("この内容でよろしいですか？（1:はい 2:いいえ）");
		if (schedule == null) {
			System.out.println("タイトル：" + title);
		}
		System.out.println("名前：" + name);
		System.out.println(startTime + " ~ " + endTime);
		System.out.println("メモ：" + memo);
		
		while (true) {
			int isComplete = Input.nextInt();
			
			if (isComplete == 1) {
				if(checkTimeReverse(startTime, endTime)) return;
				
				if (schedule == null) {
					Schedule newSchedule = new Schedule(0, SessionManager.getUser().getId(), day, title);
					ss.createSchedule(newSchedule);
					Task newTask = new Task(0, newSchedule.getId(), startTime, endTime, name, memo);
					ts.createTask(newTask);
				} else {
					Task newTask = new Task(0, schedule.getId(), startTime, endTime, name, memo);
					ts.createTask(newTask);
				}
				
				System.out.println("作成しました。");
				break;
			} else if (isComplete == 2) {
				System.out.println("作成しませんでした。");
				break;
			} else {
				System.out.println("1か2を入力してください。");
				continue;
			}
		}
	}
}
