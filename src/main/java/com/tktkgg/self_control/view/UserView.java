package com.tktkgg.self_control.view;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.service.LikeService;
import com.tktkgg.self_control.service.ScheduleService;
import com.tktkgg.self_control.service.TaskService;
import com.tktkgg.self_control.service.UserService;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.util.InputUtils;
import com.tktkgg.self_control.util.ScheduleUtils;
import com.tktkgg.self_control.util.SessionManager;

public class UserView {
	UserService us = new UserService();
	ScheduleService ss = new ScheduleService();
	TaskService ts = new TaskService();
	LikeService ls = new LikeService();
	
	public void usersView() throws ClassNotFoundException, SQLException {
		List<User> users = us.getUsers();
		while (true) {
			System.out.println("ユーザーを選択してください（番号を入力）（0で戻る）");
			System.out.println();
			
			for (User user : users) {
				System.out.println("No." + user.getId());
				System.out.println(user.getUsername());
				System.out.println();
			}
			
			int num = 0;
			while (true) {
				num = Input.nextInt();
				if (num == 0) return;
				
				if (us.getUser(num) == null) {
					System.out.println("存在しないユーザーです");
					continue;
				} else {
					break;
				}
			}
			
			userView(us.getUser(num));
		}
	}
	
	public void userView(User user) throws ClassNotFoundException, SQLException {
		System.out.println("No." + user.getId());
		System.out.println(user.getUsername());
		while (true) {
			System.out.println("どのスケジュールを確認しますか（1:月 2:火 3:水 4:木 5:金 6:土 7:日）（0で戻る）");
			int i = InputUtils.inputWeek();
			if (i == 0) break;
			
			DayOfWeek day = DayOfWeek.of(i);
			
			Schedule schedule = ss.getSpecificSchedule(user, day);
			if (ScheduleUtils.isScheduleNull(schedule)) break;
			
			List<Task> tasks = ts.getTasks(schedule.getId());
		
			System.out.println();
			
			System.out.println(schedule.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.JAPANESE) + "のスケジュール");
			System.out.println("タイトル：" + schedule.getTitle());
			for (Task task : tasks) {
				System.out.println(task.getTaskName());
				System.out.println(task.getStartTime() + " ~ " + task.getEndTime());
				System.out.println(task.getMemo());
				System.out.println();
			}
			System.out.println("いいね！：" + ls.countLikes(schedule.getId()));
			
			System.out.println();
			
			if (!ls.isLiked(SessionManager.getUser().getId(), schedule.getId())) {
				System.out.println("このユーザーのスケジュールに「いいね！」をしますか？（1:はい 2:いいえ）");
				while (true) {
					int isLike = Input.nextInt();
					if (isLike == 1) {
						ls.like(schedule.getId());
						System.out.println("「いいね！」しました");
						break;
					} else if (isLike == 2) {
						break;
					} else {
						System.out.println("1か2を入力してください。");
					}
				}
			}
		}
		
		
	}
}
