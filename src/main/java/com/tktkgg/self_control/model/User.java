package com.tktkgg.self_control.model;

import java.time.LocalDateTime;

public class User {
	private int id;
	private String username;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	
	public User(int id, String username, String email, String password, LocalDateTime createdAt) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
	}
	
	public User(String username, String email, String password) {
		this.id = 0;
		this.username = username;
		this.email = email;
		this.password = password;
		this.createdAt = LocalDateTime.now();
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setCreatedAt(LocalDateTime creatdAt) {
		this.createdAt = creatdAt;
	}
	
	@Override
	public String toString() {
		return "id=" + this.id + " username=" + this.username + " email=" + this.email + " password=" + this.password + " createdAt=" + this.createdAt;
	}
}
