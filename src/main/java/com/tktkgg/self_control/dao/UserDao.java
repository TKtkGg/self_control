package com.tktkgg.self_control.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.DBConnection;

public class UserDao {
	private User mapUser(ResultSet rs) throws SQLException {
		return new User(
			rs.getInt("id"),
			rs.getString("username"),
			rs.getString("email"),
			rs.getString("password"),
			rs.getTimestamp("created_at").toLocalDateTime()
		);
	}
	
	public User findById(int id) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM users WHERE id = ?";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, id);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapUser(rs);
				}
				
				return null;
			}
		}
	}
	
	public User findByEmail(String email) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM users WHERE email = ?";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, email);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapUser(rs);
				}
				
				return null;
			}
		}	
	}
	
	public List<User> findAll() throws ClassNotFoundException, SQLException {
		List<User> userList = new ArrayList<User>();
		String sql = "SELECT * FROM users ORDER BY id";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()) {
			
			while (rs.next()) {
				userList.add(
					mapUser(rs)
				);
			}
			
			return userList;
		}
	}
	
	public void create(User user) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getPassword());
			
			int count = pstmt.executeUpdate();

			if (count == 0) {
			    throw new SQLException("作成できませんでした");
			}
		}	
	}
	
	public void update(User user) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getPassword());
			pstmt.setInt(4, user.getId());
			
			int count = pstmt.executeUpdate();

			if (count == 0) {
			    throw new SQLException("更新できませんでした");
			}
		}	
	}
	
	public void delete(int id) throws ClassNotFoundException, SQLException {
		String sql = "DELETE FROM users WHERE id = ?";
		
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
 