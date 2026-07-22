package com.tktkgg.self_control.view;

import java.time.LocalTime;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.util.InputUtils;

public class ScheduleInputView {
	public Task scheduleInputView(Schedule schedule, String title, Task task) {
		System.out.print("名前");
		if (task != null) {
			System.out.println("：" + task.getTaskName());
		} else {
			System.out.println();
		}		System.out.print("(入力)→");
		String name = Input.nextLine();
		
		System.out.print("開始時間(時間/h)");
		if (task != null) {
			System.out.println("：" + task.getStartTime());
		} else {
			System.out.println();
		}		System.out.print("(入力)→");
		int startTimeH = InputUtils.inputHour();
		
		System.out.print("開始時間(分/m)");
		if (task != null) {
			System.out.println("：" + task.getStartTime());
		} else {
			System.out.println();
		}
		System.out.print("(入力)→");
		int startTimeM = InputUtils.inputMinute();
		
		LocalTime startTime = LocalTime.of(startTimeH, startTimeM);
		
		System.out.print("終了時間（時間/h）");
		if (task != null) {
			System.out.println("：" + task.getEndTime());
		} else {
			System.out.println();
		}
		System.out.print("(入力)→");
		int endTimeH = InputUtils.inputHour();
		
		System.out.print("終了時間（分/m）");
		if (task != null) {
			System.out.println("：" + task.getEndTime());
		} else {
			System.out.println();
		}		System.out.print("(入力)→");
		int endTimeM = InputUtils.inputMinute();
		
		LocalTime endTime = LocalTime.of(endTimeH, endTimeM);
		
		System.out.print("メモ");
		if (task != null) {
			System.out.println("：" + task.getMemo());
		} else {
			System.out.println();
		}		System.out.print("(入力)→");
		String memo = Input.nextLine();
		
		System.out.println("この内容でよろしいですか？（1:はい 2:いいえ）");
		System.out.println("タイトル：" + title);
		System.out.println("名前：" + name);
		System.out.println(startTime + " ~ " + endTime);
		System.out.println("メモ：" + memo);
		
		return new Task(0, 0, startTime, endTime, name, memo);
	}
	
	public String inputTitle(String title) {
		System.out.print("スケジュールタイトル");
		if (title != "") {
			System.out.println("：" + title);
		}
		System.out.print("(入力)→");
		return Input.nextLine();
	}
}
