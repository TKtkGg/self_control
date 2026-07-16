package com.tktkgg.self_control.util;

import java.util.Scanner;

public class Input {
	private static final Scanner sc = new Scanner(System.in);
	
	public static String next() {
		return sc.next();
	}
	
	public static int nextInt() {
		int i = 0;
		while(true) {
			try {
				i = sc.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("数字以外の値を入力しないでください。");
				sc.next();
			}
		}
		sc.nextLine();
		return i;
	}
	
	public static String nextLine() {
		return sc.nextLine();
	}
}
