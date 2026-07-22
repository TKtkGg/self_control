package com.tktkgg.self_control.service;

import java.sql.SQLException;

import com.tktkgg.self_control.dao.LikeDao;
import com.tktkgg.self_control.exception.DatabaseException;
import com.tktkgg.self_control.model.Like;
import com.tktkgg.self_control.util.SessionManager;

public class LikeService {
	private final LikeDao ld = new LikeDao();
	
	public boolean isLiked(int userId, int scheduleId) {
		try {
			return ld.exists(userId, scheduleId);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public int countLikes(int scheduleId) {
		try {
			return ld.countByScheduleId(scheduleId);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void like(int scheduleId) {
		int userId = SessionManager.getUser().getId();
		Like like = new Like(0, userId, scheduleId);
		
		try {
			if (!isLiked(like.getUserId(), like.getScheduleId())) {
				ld.create(like);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	public void unlike(int scheduleId) {
		int userId = SessionManager.getUser().getId();
		Like like = new Like(0, userId, scheduleId);
		
		try {
			if (isLiked(like.getUserId(), like.getScheduleId())) {
				ld.delete(like.getUserId(), like.getScheduleId());
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
}
