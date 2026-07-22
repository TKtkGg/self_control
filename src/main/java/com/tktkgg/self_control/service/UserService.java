package com.tktkgg.self_control.service;

import java.sql.SQLException;
import java.util.List;

import com.tktkgg.self_control.dao.UserDao;
import com.tktkgg.self_control.exception.DatabaseException;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.SessionManager;

public class UserService {
	private final UserDao ud = new UserDao();
	
	public List<User> getUsers() {
		try {
			return ud.findAll();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}	
	}
	
	public User getUser(int id) {
		try {
			return ud.findById(id);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void updateUser(User user) {
		try {
			ud.update(user);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}	
	}
	
	public User getCurrentUser(){
		return SessionManager.getUser();
	}
}
