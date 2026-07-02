package model;

import java.time.LocalDateTime;

public class User {
	private int id;
	private String name;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	
	public User(int id, String name, String email, String password, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
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
	
	public void setName(String name) {
		this.name = name;
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
		return "id=" + this.id + " name=" + this.name + " email=" + this.email + " password=" + this.password + " createdAt=" + this.createdAt;
	}
}
