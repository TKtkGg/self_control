package com.tktkgg.self_control.service;

import java.sql.SQLException;
import java.util.List;

import com.tktkgg.self_control.dao.TaskDao;
import com.tktkgg.self_control.model.Task;

public class TaskService {
	private final TaskDao td = new TaskDao();
	
	public List<Task> getTasks(int scheduleId) throws ClassNotFoundException, SQLException {
		return td.findByScheduleId(scheduleId);
	}
	
	public Task getTask(int id) throws ClassNotFoundException, SQLException {
		return td.findById(id);
	}
	
	public void createTask(Task task) throws ClassNotFoundException, SQLException {
		td.create(task);
	}
	
	public void updateTask(Task task) throws ClassNotFoundException, SQLException {
		td.update(task);
	}
	
	public void deleteTask(int id) throws ClassNotFoundException, SQLException {
		td.delete(id);
	}
}
