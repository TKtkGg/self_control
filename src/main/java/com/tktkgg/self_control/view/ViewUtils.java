package com.tktkgg.self_control.view;

import java.util.List;

import com.tktkgg.self_control.model.Task;

public class ViewUtils {
	public static void viewTask(List<Task> tasks) {
		for (Task task : tasks) {
			System.out.println(task.getId());
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
}
