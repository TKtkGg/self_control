package com.tktkgg.self_control.view;

import java.sql.SQLException;
import java.util.Scanner;

import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.service.AuthService;

public class AuthView {
	private Scanner sc = new Scanner(System.in);
	private AuthService as = new AuthService();
	
	public void homeView() throws ClassNotFoundException, SQLException {
		while(true) {
			System.out.println("1.ログイン\n2.サインアップ");
			int choice = sc.nextInt();
			
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
	
	public boolean loginView() throws ClassNotFoundException, SQLException {
		System.out.println("メールアドレスとパスワードを入力してください（qで戻る）");
		
		while(true) {
			System.out.println("メールアドレス：");
			String email = sc.next();
			if (email.equals("q")) return true;
			
			System.out.println("パスワード");
			String password = sc.next();
			
			if (as.login(email, password)) {
				System.out.println("ログインに成功しました。");
				break;
			} else {
				System.out.println("ログインに失敗しました。");
			}
		}
		return false;
	}
	
	public boolean signupView() throws ClassNotFoundException, SQLException {
		System.out.println("ユーザーネーム、メールアドレス、パスワードを入力してください。（qで戻る）");
		
		while(true) {
			System.out.println("ユーザーネーム：");
			String username = sc.next();
			if (username.equals("q")) return true;
			
			System.out.println("メールアドレス：");
			String email = sc.next();
			
			System.out.println("パスワード");
			String password = sc.next();
			
			User user = new User(username, email, password);
			as.signup(user);
			break;
		}
		return false;
	}
}
