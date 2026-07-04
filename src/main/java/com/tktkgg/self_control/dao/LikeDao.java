package com.tktkgg.self_control.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tktkgg.self_control.model.Like;
import com.tktkgg.self_control.util.DBConnection;

public class LikeDao {
	public List<Like> findByScheduleId(int scheduleId) throws  ClassNotFoundException, SQLException {
		List<Like> likeList = new ArrayList<Like>();
		
		String sql = "SELECT * FROM likes WHERE schedule_id = ?";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, scheduleId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					likeList.add(
						new Like(
							rs.getInt("id"),
							rs.getInt("user_id"),
							rs.getInt("schedule_id")
							
						)
					);
				}
				
				return likeList;
			}
		}
	}
	
	public int countByScheduleId(int scheduleId) throws  ClassNotFoundException, SQLException {
		int likeCount = 0;
		
		String sql = "SELECT COUNT(*) AS like_count FROM likes WHERE schedule_id = ?";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, scheduleId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("like_count");
				}
				
				return likeCount;
			}
		}
	}
}
