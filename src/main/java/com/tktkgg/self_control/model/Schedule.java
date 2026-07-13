package com.tktkgg.self_control.model;

import java.time.DayOfWeek;

public class Schedule {
	private int id;
	private int userId;
	private DayOfWeek dayOfWeek;
	private String title;
	
	public Schedule(int id, int userId, DayOfWeek dayOfWeek, String title) {
		this.id = id;
		this.userId = userId;
		this.dayOfWeek = dayOfWeek;
		this.title = title;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public DayOfWeek getDayOfWeek() {
		return this.dayOfWeek;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "id=" + this.id + " userId=" + this.userId + " dayOfWeek=" + this.dayOfWeek + "title=" + this.title;
	}
}
