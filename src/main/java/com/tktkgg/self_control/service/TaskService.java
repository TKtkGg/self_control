package com.tktkgg.self_control.service;

import java.sql.SQLException;
import java.util.List;

import com.tktkgg.self_control.dao.TaskDao;
import com.tktkgg.self_control.exception.DatabaseException;
import com.tktkgg.self_control.model.Task;

public class TaskService {
	private final TaskDao td = new TaskDao();
	
	public List<Task> getTasks(int scheduleId) {
		try {
			return td.findByScheduleId(scheduleId);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public Task getTask(int id) {
		try {
			return td.findById(id);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void createTask(Task task) {
		try {
			td.create(task);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void updateTask(Task task) {
		try {
			td.update(task);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void deleteTask(int id) {
		try {
			td.delete(id);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
}
