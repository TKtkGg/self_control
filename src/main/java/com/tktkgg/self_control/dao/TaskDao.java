package com.tktkgg.self_control.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.util.DBConnection;

public class TaskDao {
	public List<Task> findByScheduleId(int scheduleId) throws ClassNotFoundException, SQLException {
		List<Task> taskList = new ArrayList<Task>();
		
		String sql = "SELECT * FROM tasks WHERE schedule_id = ? ORDER BY start_time";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, scheduleId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					taskList.add(
						new Task(
							rs.getInt("id"),
							rs.getInt("schedule_id"),
							rs.getTime("start_time").toLocalTime(),
							rs.getTime("end_time").toLocalTime(),
							rs.getString("task_name"),
							rs.getString("memo")
						)
					);
				}
				
				return taskList;
			}
		}
	}
}
