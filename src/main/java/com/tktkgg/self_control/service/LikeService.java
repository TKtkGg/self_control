package com.tktkgg.self_control.service;

import java.sql.SQLException;

import com.tktkgg.self_control.dao.LikeDao;
import com.tktkgg.self_control.model.Like;
import com.tktkgg.self_control.util.SessionManager;

public class LikeService {
	private final LikeDao ld = new LikeDao();
	
	public boolean isLiked(int userId, int scheduleId) throws ClassNotFoundException, SQLException {
		return ld.exists(userId, scheduleId);
	}
	
	public int countLikes(int scheduleId) throws ClassNotFoundException, SQLException {
		return ld.countByScheduleId(scheduleId);
	}
	
	public void like(int scheduleId) throws ClassNotFoundException, SQLException {
		int userId = SessionManager.getUser().getId();
		Like like = new Like(0, userId, scheduleId);
		
		if (!isLiked(like.getUserId(), like.getScheduleId())) {
			ld.create(like);
		}
	}
	
	public void unlike(int scheduleId) throws ClassNotFoundException, SQLException {
		int userId = SessionManager.getUser().getId();
		Like like = new Like(0, userId, scheduleId);
		
		if (isLiked(like.getUserId(), like.getScheduleId())) {
			ld.delete(like.getUserId(), like.getScheduleId());
		}
	}
}
