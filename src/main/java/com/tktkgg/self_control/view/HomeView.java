package com.tktkgg.self_control.view;

import com.tktkgg.self_control.service.AuthService;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.util.SessionManager;

public class HomeView {
	ScheduleView sv = new ScheduleView();
	UserView uv = new UserView();
	AuthService as = new AuthService();
	
	public void homeView() {
		while(true) {
			System.out.println("ホーム");
			System.out.println("1.今日のスケジュールの確認\n2.スケジュールの編集\n3.スケジュールの追加\n4.ユーザー一覧\n5.ログアウト");
			int choice = Input.nextInt();
			
			switch(choice) {
				case 1:
					sv.checkScheduleView();
					break;
				case 2:
					sv.editScheduleView();
					break;
				case 3:
					sv.addScheduleView();
					break;
				case 4:
					uv.usersView();
					break;
				case 5:
					as.logout(SessionManager.getUser());
					break;
				default:
					break;
			}
			
			if (SessionManager.getUser() == null) {
				break;
			}
		}
		
			
	}
}
