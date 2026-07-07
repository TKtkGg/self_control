package com.tktkgg.self_control.service;

import java.sql.SQLException;

import com.tktkgg.self_control.dao.UserDao;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.SessionManager;

public class AuthService {
	UserDao ud = new UserDao();
	SessionManager sm = new SessionManager();
	public boolean login(String email, String password) throws ClassNotFoundException, SQLException {
		User user = ud.findByEmail(email);
		
		if (user == null) return false;
		
		if (user.getPassword().equals(password)) {
			sm.setUser(user);
			return true;
		} else {
			return false;
		}
	}
	
	public void signup(User user) throws ClassNotFoundException, SQLException {
		ud.create(user);
		sm.setUser(user);
	}
	
	public void logout(User user) {
		sm.clearUser();
	}
	
}
