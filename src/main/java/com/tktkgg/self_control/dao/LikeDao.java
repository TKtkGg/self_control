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
	private Like mapLike(ResultSet rs) throws SQLException {
		return new Like(
			rs.getInt("id"),
			rs.getInt("user_id"),
			rs.getInt("schedule_id")	
		);
	}
	
	public List<Like> findByScheduleId(int scheduleId) throws SQLException {
		List<Like> likeList = new ArrayList<Like>();
		
		String sql = "SELECT * FROM likes WHERE schedule_id = ?";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, scheduleId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					likeList.add(
						mapLike(rs)
					);
				}
				
				return likeList;
			}
		}
	}
	
	public boolean exists(int userId, int scheduleId) throws SQLException {
		String sql = "SELECT EXISTS(SELECT 1 FROM likes WHERE user_id = ? AND schedule_id = ?) AS liked";
		
		try(Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, userId);
			pstmt.setInt(2, scheduleId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getBoolean("liked");
				}
				
				return false;
			}
		}
	}
	
	public int countByScheduleId(int scheduleId) throws SQLException {
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
	
	public void create(Like like) throws SQLException {
		String sql = "INSERT INTO likes(user_id, schedule_id) VALUES(?, ?)";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, like.getUserId());
			pstmt.setInt(2, like.getScheduleId());
			
			int count = pstmt.executeUpdate();

			if (count == 0) {
			    throw new SQLException("作成できませんでした");
			}
		}	
	}
	
	public void delete(int userId, int scheduleId) throws SQLException {
		String sql = "DELETE FROM likes WHERE user_id = ? AND schedule_id = ?";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, userId);
			pstmt.setInt(2, scheduleId);
			
			int count = pstmt.executeUpdate();

			if (count == 0) {
			    throw new SQLException("削除できませんでした");
			}
		}	
	}
}
