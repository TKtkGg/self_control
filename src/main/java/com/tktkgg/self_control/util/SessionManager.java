package com.tktkgg.self_control.util;

import com.tktkgg.self_control.model.User;

public class SessionManager {
	private static User currentUser;
	
	public static User getUser() {
		return SessionManager.currentUser;
	}
	
	public static void setUser(User user) {
		SessionManager.currentUser = user;
	}
	
	public static void clearUser() {
		SessionManager.currentUser = null;
	}
}
