package model;

public class Like {
	private int id;
	private int userId;
	private int scheduleId;
	
	public Like(int id, int userId, int scheduleId) {
		this.id = id;
		this.userId = userId;
		this.scheduleId = scheduleId;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public int getScheduleId() {
		return this.scheduleId;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	@Override
	public String toString() {
		return "id=" + this.id + " userId=" + this.userId + " scheduleId=" + this.scheduleId;
	}
}
