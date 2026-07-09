package com.tktkgg.self_control.service;

import java.sql.SQLException;

import com.tktkgg.self_control.dao.UserDao;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.SessionManager;

public class AuthService {
	UserDao ud = new UserDao();
	
	public boolean login(String email, String password) throws ClassNotFoundException, SQLException {
		User user = ud.findByEmail(email);
		
		if (user == null) return false;
		
		if (user.getPassword().equals(password)) {
			SessionManager.setUser(user);
			return true;
		} else {
			return false;
		}
	}
	
	public void signup(User user) throws ClassNotFoundException, SQLException {
		ud.create(user);
		SessionManager.setUser(user);
	}
	
	public void logout(User user) {
		SessionManager.clearUser();
	}
	
}
