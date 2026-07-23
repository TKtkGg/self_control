package com.tktkgg.self_control.view.user;

import java.util.List;

import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.service.UserService;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.view.MenuAction;
import com.tktkgg.self_control.view.ViewUtils;

public class UsersView implements MenuAction {
	private final UserService us = new UserService();
	private final UserView uv = new UserView();
	
	public void execute() {
		List<User> users = us.getUsers();
		while (true) {
			System.out.println("ユーザーを選択してください（番号を入力）（0で戻る）");
			System.out.println();
			
			ViewUtils.viewUsers(users);
			
			int num = 0;
			while (true) {
				num = Input.nextInt();
				if (num == 0) return;
				
				User user = us.getUser(num);
				
				if (user == null) {
					System.out.println("存在しないユーザーです");
					continue;
				} else {
					uv.userView(user);
					break;
				}
			}
		}
	}
}
