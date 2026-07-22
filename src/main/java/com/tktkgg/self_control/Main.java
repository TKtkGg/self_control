package com.tktkgg.self_control;

import com.tktkgg.self_control.exception.DatabaseException;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.view.AuthView;
import com.tktkgg.self_control.view.HomeView;

public class Main {
	public static void main(String[] args) {
		AuthView authView = new AuthView();
		HomeView homeView = new HomeView();
		
		while(true) {
			try {
				authView.startView();
				homeView.homeView();
			} catch (DatabaseException e) {
				System.out.println("データベースエラーが発生しました");
			}
			
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
