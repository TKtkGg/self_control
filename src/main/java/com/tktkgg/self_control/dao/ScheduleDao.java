package com.tktkgg.self_control.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.util.DBConnection;

public class ScheduleDao {
	public List<Schedule> findByUserId(int userId) throws ClassNotFoundException, SQLException {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		
		String sql = "SELECT * FROM schedules WHERE user_id = ?";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()) {
			
			pstmt.setInt(1, userId);
			
			while(rs.next()) {
				scheduleList.add(
					new Schedule(
						rs.getInt("id"),
						rs.getInt("user_id"),
						rs.getString("day_of_week"),
						rs.getString("title")
					)
				);
			}
			
			return scheduleList;
		}
	}
}
