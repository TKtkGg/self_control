package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		final String URL = "jdbc:postgresql://localhost:5432/self_controldb";
		final String USER = "miyamotoyuuki";
		final String PASSWORD = "postgres";
		
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
		return con;
	}
}
