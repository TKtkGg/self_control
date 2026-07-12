package com.tktkgg.self_control;

import java.sql.SQLException;

import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.view.AuthView;
import com.tktkgg.self_control.view.HomeView;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		AuthView authView = new AuthView();
		HomeView homeView = new HomeView();
		
		while(true) {
			authView.startView();
			homeView.homeView();
			System.out.println("アプリケーションを終了しますか？(1.はい 2.いいえ)");
			int isFinish = Input.nextInt();
			if (isFinish == 1) {
				break;
			} else {
				continue;
			}
		}
		
		
		System.out.println("成功");
	}

}
