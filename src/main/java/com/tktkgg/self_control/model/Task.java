package com.tktkgg.self_control.model;

import java.time.LocalTime;

public class Task {
	private int id;
	private int scheduleId;
	private LocalTime startTime;
	private LocalTime endTime;
	private String taskName;
	private String memo;
	
	public Task(int id, int scheduleId, LocalTime startTime, LocalTime endTime, String taskName, String memo) {
		this.id = id;
		this.scheduleId = scheduleId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.taskName = taskName;
		this.memo = memo;
	}
	
	public int getId() {
		return this.id;	
	}
	
	public int getScheduleId() {
		return this.scheduleId;
	}
	
	public LocalTime getStartTime() {
		return this.startTime;
	}
	
	public LocalTime getEndTime() {
		return this.endTime;
	}
	
	public String getTaskName() {
		return this.taskName;
	}
	
	public String getMemo() {
		return this.memo;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public String toString() {
		return "id=" + this.id + " scheduleId=" + this.scheduleId + " startTime=" + this.startTime + " endTime=" + this.endTime + " taskName=" + this.taskName + " memo=" + this.memo;
	}
	
	
	public boolean isTimeValid() {
		return startTime.isBefore(endTime);
	}
	
	public String getTimeRange() {
		return startTime + " ~ " + endTime;
	}
	
	public boolean belongsTo(Schedule schedule) {
		return this.getScheduleId() == schedule.getId();
	}
}

