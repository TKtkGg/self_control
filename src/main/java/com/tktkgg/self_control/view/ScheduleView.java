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
import com.tktkgg.self_control.util.SessionManager;

public class ScheduleView {
	ScheduleService ss = new ScheduleService();
	TaskService ts = new TaskService();
	public void checkScheduleView(boolean isToday) throws ClassNotFoundException, SQLException {
		Schedule schedule = null;
		List<Task> tasks = null;
		if (isToday) {
			schedule = ss.getTodaySchedule();
			tasks = ts.getTasks(schedule.getId());
		} else {
			System.out.println("何曜日のスケジュールを確認しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）");
			int i = Input.nextInt();
			DayOfWeek day = DayOfWeek.of(i);
			
			schedule = ss.getSpecificSchedule(day);
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
		int i = Input.nextInt();
		DayOfWeek day = DayOfWeek.of(i);
		
		Schedule schedule = ss.getSpecificSchedule(day);
		List<Task> tasks = ts.getTasks(schedule.getId());
		
		System.out.println("スケジュールタイトル：" + schedule.getTitle());
		System.out.print("(入力)→");
		String title = Input.next();
		
		for (Task task : tasks) {
			System.out.println(task.getId());
			System.out.println(task.getTaskName());
			System.out.println(task.getStartTime() + " ~ " + task.getEndTime());
		}
		System.out.println("どのタスクを編集しますか？（番号を入力）");
		int id = Input.nextInt();
		
		Task task = ts.getTask(id);
		
		System.out.println("名前：" + task.getTaskName());
		System.out.print("(入力)→");
		String name = Input.next();
		
		System.out.println("開始時間(時間/h)：" + task.getStartTime());
		System.out.print("(入力)→");
		int startTimeH = Input.nextInt();
		
		System.out.println("開始時間(分/m)：" + task.getStartTime());
		System.out.print("(入力)→");
		int startTimeM = Input.nextInt();
		
		LocalTime startTime = LocalTime.of(startTimeH, startTimeM);
		
		System.out.println("終了時間（時間/h）：" + task.getEndTime());
		System.out.print("(入力)→");
		int endTimeH = Input.nextInt();
		
		System.out.println("終了時間（分/m）：" + task.getEndTime());
		System.out.print("(入力)→");
		int endTimeM = Input.nextInt();
		
		LocalTime endTime = LocalTime.of(endTimeH, endTimeM);
		
		System.out.println("メモ：" + task.getMemo());
		System.out.print("(入力)→");
		String memo = Input.next();
		
		System.out.println("この変更でよろしいですか？（1:はい 2:いいえ）");
		System.out.println("タイトル：" + title);
		System.out.println("名前：" + name);
		System.out.println(startTime + " ~ " + endTime);
		System.out.println("メモ：" + memo);
		
		int isComplete = Input.nextInt();
		
		Schedule newSchedule = new Schedule(schedule.getId(), SessionManager.getUser().getId(), day, title);
		Task newTask = new Task(task.getId(), schedule.getId(), startTime, endTime, name, memo);
		ss.updateSchedule(newSchedule);
		ts.updateTask(newTask);
		
		System.out.println("更新が完了しました。");
		
	}
	
	public void addScheduleView() throws ClassNotFoundException, SQLException {
		System.out.println("何曜日に追加しますか？（1:月 2:火 3:水 4:木 5:金 6:土 7:日）");
		int i = Input.nextInt();
		DayOfWeek day = DayOfWeek.of(i);
		Schedule schedule = ss.getSpecificSchedule(day);
		String title = null;
		if (schedule == null) {
			System.out.print("スケジュールタイトル：");
			title = Input.next();
		}
		
		System.out.print("名前：");
		String name = Input.next();
		
		System.out.print("開始時間(時間/h)：");
		int startTimeH = Input.nextInt();
		
		System.out.print("開始時間(分/m)：");
		int startTimeM = Input.nextInt();
		
		LocalTime startTime = LocalTime.of(startTimeH, startTimeM);
		
		System.out.print("終了時間（時間/h）：");
		int endTimeH = Input.nextInt();
		
		System.out.print("終了時間（分/m）：");
		int endTimeM = Input.nextInt();
		
		LocalTime endTime = LocalTime.of(endTimeH, endTimeM);
		
		System.out.print("メモ：");
		String memo = Input.next();
		
		System.out.println("この内容でよろしいですか？（1:はい 2:いいえ）");
		if (schedule == null) {
			System.out.println("タイトル：" + title);
		}
		System.out.println("名前：" + name);
		System.out.println(startTime + " ~ " + endTime);
		System.out.println("メモ：" + memo);
		
		int isComplete = Input.nextInt();
		
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
	}
}
