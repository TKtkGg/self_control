package com.tktkgg.self_control.view;

import java.sql.SQLException;

import com.tktkgg.self_control.exception.InvalidTimeException;
import com.tktkgg.self_control.service.AuthService;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.util.SessionManager;

public class HomeView {
	private final ScheduleView sv = new ScheduleView();
	private final UserView uv = new UserView();
	private final AuthService as = new AuthService();
	
	public void homeView() throws ClassNotFoundException, SQLException, InvalidTimeException {
		while(true) {
			System.out.println("ホーム");
			System.out.println("1.今日のスケジュールの確認\n2.スケジュールの確認\n3.スケジュールの編集\n4.スケジュールの追加\n5.ユーザー一覧\n6.ログアウト");
			int choice = Input.nextInt();
			
			switch(choice) {
				case 1:
					sv.checkScheduleView(true);
					break;
				case 2:
					sv.checkScheduleView(false);
					break;
				case 3:
					sv.editScheduleView();
					break;
				case 4:
					sv.addScheduleView();
					break;
				case 5:
					uv.usersView();
					break;
				case 6:
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
