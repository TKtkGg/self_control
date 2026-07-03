package com.tktkgg.self_control.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.util.DBConnection;

public class UserDao {
	public User findByEmail(String email) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM users WHERE email = ?";
		
		try (Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
						
			if (rs.next()) {
				return new User(
					rs.getInt("id"),
					rs.getString("username"),
					rs.getString("email"),
					rs.getString("password"),
					rs.getTimestamp("created_at").toLocalDateTime()
				);
			}
			
			return null;
			
		}	
	}
}
