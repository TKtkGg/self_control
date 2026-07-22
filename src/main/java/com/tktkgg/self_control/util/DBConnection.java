package com.tktkgg.self_control.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public static Connection getConnection() throws SQLException {
		final String URL = "jdbc:postgresql://localhost:5432/self_control";
		final String USER = "self_control_user";
		final String PASSWORD = "postgres";
		
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
