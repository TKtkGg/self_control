package com.tktkgg.self_control;

import java.sql.SQLException;

import com.tktkgg.self_control.view.AuthView;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		AuthView authView = new AuthView();
		authView.homeView();
		System.out.println("成功");
	}

}
