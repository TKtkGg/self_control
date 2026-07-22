package com.tktkgg.self_control.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.util.DBConnection;

public class TaskDao {
	private Task mapTask(ResultSet rs) throws SQLException {
		return new Task(
			rs.getInt("id"),
			rs.getInt("schedule_id"),
			rs.getTime("start_time").toLocalTime(),
			rs.getTime("end_time").toLocalTime(),
			rs.getString("task_name"),
			rs.getString("memo")
		);
	}
	
	public Task findById(int id) throws SQLException {
		String sql = "SELECT * FROM tasks WHERE id = ?";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, id);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapTask(rs);
				}
				
				return null;
			}
		}
	}
	
	public List<Task> findByScheduleId(int scheduleId) throws SQLException {
		List<Task> taskList = new ArrayList<Task>();
		
		String sql = "SELECT * FROM tasks WHERE schedule_id = ? ORDER BY start_time";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, scheduleId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					taskList.add(
						mapTask(rs)
					);
				}
				
				return taskList;
			}
		}
	}
	
	public void create(Task task) throws SQLException {
		String sql = "INSERT INTO tasks(schedule_id, start_time, end_time, task_name, memo) VALUES(?, ?, ?, ?, ?)";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, task.getScheduleId());
			pstmt.setTime(2, Time.valueOf(task.getStartTime()));
			pstmt.setTime(3, Time.valueOf(task.getEndTime()));
			pstmt.setString(4, task.getTaskName());
			pstmt.setString(5, task.getMemo());
			
			int count = pstmt.executeUpdate();

			if (count == 0) {
			    throw new SQLException("作成できませんでした");
			}
		}
	}

	public void update(Task task) throws SQLException {
		String sql = "UPDATE tasks SET schedule_id = ?, start_time = ?, end_time = ?, task_name = ?, memo = ? WHERE id = ?";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, task.getScheduleId());
			pstmt.setTime(2, Time.valueOf(task.getStartTime()));
			pstmt.setTime(3, Time.valueOf(task.getEndTime()));
			pstmt.setString(4, task.getTaskName());
			pstmt.setString(5, task.getMemo());
			pstmt.setInt(6, task.getId());
			
			int count = pstmt.executeUpdate();

			if (count == 0) {
			    throw new SQLException("更新できませんでした");
			}
		}
	}

	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM tasks WHERE id = ?";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, id);
			
			int count = pstmt.executeUpdate();

			if (count == 0) {
			    throw new SQLException("削除できませんでした");
			}
		}	
	}
}
