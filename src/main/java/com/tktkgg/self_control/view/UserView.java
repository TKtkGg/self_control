package com.tktkgg.self_control.view;

import java.sql.SQLException;
import java.util.List;

import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.service.UserService;
import com.tktkgg.self_control.util.Input;

public class UserView {
	UserService us = new UserService();
	
	public void usersView() throws ClassNotFoundException, SQLException {
		List<User> users = us.getUsers();
		System.out.println("ユーザーを選択してください（番号を入力）");
		System.out.println();
		
		for (User user : users) {
			System.out.println("No." + user.getId());
			System.out.println(user.getUsername());
			System.out.println();
		}
		
		int num = 0;
		while (true) {
			num = Input.nextInt();
			if (us.getUser(num) == null) {
				System.out.println("存在しないユーザーです");
				continue;
			} else {
				break;
			}
		}
		
		userView(us.getUser(num));
	}
	
	public void userView(User user) throws ClassNotFoundException, SQLException {
		System.out.println("成功");
	}
}
