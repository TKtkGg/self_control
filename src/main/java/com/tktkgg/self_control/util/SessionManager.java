package com.tktkgg.self_control.util;

import com.tktkgg.self_control.model.User;

public class SessionManager {
	private static User currentUser;
	
	public User getUser() {
		return SessionManager.currentUser;
	}
	
	public void setUser(User user) {
		SessionManager.currentUser = user;
	}
	
	public void clearUser() {
		SessionManager.currentUser = null;
	}
}
