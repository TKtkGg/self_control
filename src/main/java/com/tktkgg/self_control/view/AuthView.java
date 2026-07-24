package com.tktkgg.self_control.view;

import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.service.AuthService;
import com.tktkgg.self_control.util.Input;

public class AuthView {
	private final AuthService as = new AuthService();
	
	public void startView() {
		while(true) {
			System.out.println("1.ログイン\n2.サインアップ");
			int choice = Input.nextInt();
			
			if (choice == 1) {
				if (loginView()) {
					continue;
				} else {
					break;
				}
			} else if (choice == 2) {
				if (signupView()) {
					continue;
				} else {
					break;
				}
			}
		}
		
	}
	
	public boolean loginView() {
		System.out.println("メールアドレスとパスワードを入力してください（qで戻る）");
		
		while(true) {
			System.out.print("メールアドレス：");
			String email = Input.next();
			if (email.equals("q")) return true;
			
			System.out.print("パスワード：");
			String password = Input.next();
			
			if (as.login(email, password)) {
				System.out.println("ログインに成功しました。");
				break;
			} else {
				System.out.println("ログインに失敗しました。");
			}
		}
		return false;
	}
	
	public boolean signupView() {
		System.out.println("ユーザーネーム、メールアドレス、パスワードを入力してください。（qで戻る）");
		
		while(true) {
			System.out.print("ユーザーネーム：");
			String username = Input.next();
			if (username.equals("q")) return true;
			
			System.out.print("メールアドレス：");
			String email = Input.next();
			
			System.out.print("パスワード：");
			String password = Input.next();
			
			User user = new User(username, email, password);
			as.signup(user);
			break;
		}
		return false;
	}
}
