package com.tktkgg.self_control.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.util.DBConnection;

public class ScheduleDao {
	private Schedule mapSchedule(ResultSet rs) throws SQLException {
		return new Schedule(
			rs.getInt("id"),
			rs.getInt("user_id"),
			DayOfWeek.of(rs.getInt("day_of_week")),
			rs.getString("title")
		);
	}
	
	public Schedule findById(int id) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM schedules WHERE id = ?";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, id);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapSchedule(rs);
				}
				
				return null;
			}
		}
	}
	
	public List<Schedule> findByUserId(int userId) throws ClassNotFoundException, SQLException {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		
		String sql = "SELECT * FROM schedules WHERE user_id = ?";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, userId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					scheduleList.add(
						mapSchedule(rs)
					);
				}
				
				return scheduleList;
			}
		}
	}
	
	public Schedule findByUserIdAndDayOfWeek(int userId, DayOfWeek dayOfWeek) throws ClassNotFoundException, SQLException {
		
		String sql = "SELECT * FROM schedules WHERE user_id = ? AND day_of_week = ?";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, userId);
			pstmt.setInt(2, dayOfWeek.getValue());
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					return mapSchedule(rs);
				}
				
				return null;
			}
		}
	} 
	
	public List<Schedule> findAll() throws ClassNotFoundException, SQLException {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		
		String sql = "SELECT * FROM schedules ORDER BY id";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()) {
					
			while(rs.next()) {
				scheduleList.add(
					mapSchedule(rs)
				);
			}
			
			return scheduleList;
			
		}
	}
	
	public Schedule create(Schedule schedule) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO schedules(user_id, day_of_week, title) VALUES(?, ?, ?)";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			pstmt.setInt(1, schedule.getUserId());
			pstmt.setInt(2, schedule.getDayOfWeek().getValue());
			pstmt.setString(3, schedule.getTitle());
			
			int count = pstmt.executeUpdate();
			if (count == 0) {
			    throw new SQLException("作成できませんでした");
			}
			
			try (ResultSet rs = pstmt.getGeneratedKeys();) {
				if (rs.next()) {
					schedule.setId(rs.getInt(1));
					return schedule;
				}
			}
			throw new SQLException("IDの取得に失敗しました");
			
		}
	}

	public void update(Schedule schedule) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE schedules SET user_id = ?, day_of_week = ?, title = ? WHERE id = ?";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, schedule.getUserId());
			pstmt.setInt(2, schedule.getDayOfWeek().getValue());
			pstmt.setString(3, schedule.getTitle());
			pstmt.setInt(4, schedule.getId());
			
			int count = pstmt.executeUpdate();

			if (count == 0) {
			    throw new SQLException("更新できませんでした");
			}
		}
	}

	public void delete(int id) throws ClassNotFoundException, SQLException {
		String sql = "DELETE FROM schedules WHERE id = ?";
		
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
