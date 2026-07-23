package com.tktkgg.self_control.view;

import java.util.HashMap;
import java.util.Map;

import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.util.SessionManager;
import com.tktkgg.self_control.view.schedule.AddScheduleView;
import com.tktkgg.self_control.view.schedule.CheckScheduleView;
import com.tktkgg.self_control.view.schedule.EditScheduleView;
import com.tktkgg.self_control.view.user.UsersView;

public class HomeView {
	Map<Integer, MenuAction> menu = new HashMap<>();
	
	public HomeView() {
		menu.put(1, new CheckScheduleView(true));
		menu.put(2, new CheckScheduleView(false));
		menu.put(3, new EditScheduleView());
		menu.put(4, new AddScheduleView());
		menu.put(5, new UsersView());
		menu.put(6, new LogoutView());
	}
	
	public void homeView() {
		while(true) {
			System.out.println("ホーム");
			System.out.println("1.今日のスケジュールの確認\n2.スケジュールの確認\n3.スケジュールの編集\n4.スケジュールの追加\n5.ユーザー一覧\n6.ログアウト");
			
			menu.get(Input.nextInt()).execute();
			
			if (SessionManager.getUser() == null) {
				break;
			}
		}
		
			
	}
}
