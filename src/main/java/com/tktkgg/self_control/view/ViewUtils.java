package com.tktkgg.self_control.view;

import java.util.List;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.model.User;

public class ViewUtils {
	public static void viewTask(List<Task> tasks) {
		for (Task task : tasks) {
			System.out.println(task.getTaskName());
			System.out.println(task.getTimeRange());
			System.out.println(task.getMemo());
			System.out.println();
		}
	}
	
	public static void viewTaskWithId(List<Task> tasks) {
		for (Task task : tasks) {
			System.out.println(task.getId());
			System.out.println(task.getTaskName());
			System.out.println(task.getTimeRange());
			System.out.println();
		}
	}
	
	public static void viewUsers(List<User> users) {
		for (User user : users) {
			System.out.println("No." + user.getId());
			System.out.println(user.getUsername());
			System.out.println();
		}
	}
	
	public static void viewTitle(Schedule schedule, List<Task> tasks) {
		System.out.println(schedule.getDisplayDay() + "のスケジュール");
		System.out.println("タイトル：" + schedule.getTitle());
		ViewUtils.viewTask(tasks);
	}
}
