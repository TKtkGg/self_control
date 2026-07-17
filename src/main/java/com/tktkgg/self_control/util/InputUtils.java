package com.tktkgg.self_control.util;

public class InputUtils {
	public static int inputWeek() {
		int i = 0;
		while (true) {
			i = Input.nextInt();
			if (i < 0 || i > 7) {
				System.out.println("0~7で入力してください。");
				continue;
			} else {
				break;
			}
		}
		
		return i;
	}
	
	public static int inputHour() {
		int i = 0;
		while (true) {
			i = Input.nextInt();
			if (i < 0 || i > 23) {
				System.out.println("0~23で入力してください。");
				continue;
			} else {
				break;
			}
		}
		
		return i;
	}
	
	public static int inputMinute() {
		int i = 0;
		while (true) {
			i = Input.nextInt();
			if (i < 0 || i > 59) {
				System.out.println("0~59で入力してください。");
				continue;
			} else {
				break;
			}
		}
		
		return i;
	}
}
