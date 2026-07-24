package com.tktkgg.self_control.view;

import java.util.List;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.Input;

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
	
	public static boolean confirm(String action) {
		while (true) {
	        int input = Input.nextInt();
	        
	        if (input == 1) {
	        	System.out.println(action + "しました");
	        	System.out.println();
	        	return true;
	        } else if (input == 2) {
	        	System.out.println(action + "しませんでした");
	        	System.out.println();
	        	return false;
	        } else {
	        	System.out.println("1か2を入力してください。");
	        }  
	    }
	}
}
