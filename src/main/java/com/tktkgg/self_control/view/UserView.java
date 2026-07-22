package com.tktkgg.self_control.view;

import java.time.DayOfWeek;
import java.util.List;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.service.LikeService;
import com.tktkgg.self_control.service.ScheduleService;
import com.tktkgg.self_control.service.TaskService;
import com.tktkgg.self_control.service.UserService;
import com.tktkgg.self_control.util.Input;
import com.tktkgg.self_control.util.InputUtils;
import com.tktkgg.self_control.util.SessionManager;

public class UserView {
	private final UserService us = new UserService();
	private final ScheduleService ss = new ScheduleService();
	private final TaskService ts = new TaskService();
	private final LikeService ls = new LikeService();
	
	private void likeView(Schedule schedule) {
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
	
	public void usersView() {
		List<User> users = us.getUsers();
		while (true) {
			System.out.println("ユーザーを選択してください（番号を入力）（0で戻る）");
			System.out.println();
			
			ViewUtils.viewUsers(users);
			
			int num = 0;
			while (true) {
				num = Input.nextInt();
				if (num == 0) return;
				
				User user = us.getUser(num);
				
				if (user == null) {
					System.out.println("存在しないユーザーです");
					continue;
				} else {
					userView(user);
					break;
				}
			}
		}
	}
	
	public void userView(User user) {
		System.out.println("No." + user.getId());
		System.out.println(user.getUsername());
		while (true) {
			System.out.println("どのスケジュールを確認しますか（1:月 2:火 3:水 4:木 5:金 6:土 7:日）（0で戻る）");
			int i = InputUtils.inputWeek();
			if (i == 0) break;
			
			DayOfWeek day = DayOfWeek.of(i);
			
			Schedule schedule = ss.getSpecificSchedule(user, day);
			if (schedule == null) {
				System.out.println("スケジュールが存在しません");
				continue;
			}
			
			List<Task> tasks = ts.getTasks(schedule.getId());
		
			System.out.println();
			
			ViewUtils.viewTitle(schedule, tasks);
			
			int likeCount = ls.countLikes(schedule.getId());
			System.out.println("いいね！：" + likeCount);
			
			System.out.println();
			
			likeView(schedule);
		}
		
		
	}
}
