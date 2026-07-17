package com.tktkgg.self_control.util;

import com.tktkgg.self_control.model.Schedule;

public class ScheduleUtils {
	public static boolean isScheduleNull(Schedule schedule) {
		if (schedule == null) {
			System.out.println("スケジュールが存在していません。");
			return true;
		}
		return false;
	}
}
