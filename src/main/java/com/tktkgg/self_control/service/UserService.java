package com.tktkgg.self_control.service;

import java.sql.SQLException;
import java.util.List;

import com.tktkgg.self_control.dao.UserDao;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.SessionManager;

public class UserService {
	private final UserDao ud = new UserDao();
	
	public List<User> getUsers() throws ClassNotFoundException, SQLException {
		return ud.findAll();
	}
	
	public User getUser(int id) throws ClassNotFoundException, SQLException {
		return ud.findById(id);
	}
	
	public void updateUser(User user) throws ClassNotFoundException, SQLException {
		ud.update(user);
	}
	
	public User getCurrentUser(){
		return SessionManager.getUser();
	}
}
