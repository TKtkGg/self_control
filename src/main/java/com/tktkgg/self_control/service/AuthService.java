package com.tktkgg.self_control.service;

import java.sql.SQLException;

import com.tktkgg.self_control.dao.UserDao;
import com.tktkgg.self_control.exception.DatabaseException;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.SessionManager;

public class AuthService {
	private final UserDao ud = new UserDao();
	
	public boolean login(String email, String password) {
		User user = null;
		try {
			user = ud.findByEmail(email);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
		if (user == null) return false;
		
		if (user.getPassword().equals(password)) {
			SessionManager.setUser(user);
			return true;
		} else {
			return false;
		}
	}
	
	public void signup(User user) {
		try {
			ud.create(user);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
		SessionManager.setUser(user);
	}
	
	public void logout(User user) {
		SessionManager.clearUser();
	}
	
}
