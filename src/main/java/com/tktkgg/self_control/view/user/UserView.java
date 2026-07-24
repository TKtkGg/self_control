package com.tktkgg.self_control.view.user;

import java.time.DayOfWeek;
import java.util.List;

import com.tktkgg.self_control.model.Schedule;
import com.tktkgg.self_control.model.Task;
import com.tktkgg.self_control.model.User;
import com.tktkgg.self_control.service.LikeService;
import com.tktkgg.self_control.service.ScheduleService;
import com.tktkgg.self_control.service.TaskService;
import com.tktkgg.self_control.util.InputUtils;
import com.tktkgg.self_control.util.SessionManager;
import com.tktkgg.self_control.view.ViewUtils;

public class UserView {
	private final ScheduleService ss = new ScheduleService();
	private final TaskService ts = new TaskService();
	private final LikeService ls = new LikeService();
	
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
			
			if (!ls.isLiked(SessionManager.getUser().getId(), schedule.getId())) {
				System.out.println("このユーザーのスケジュールに「いいね！」をしますか？（1:はい 2:いいえ）");
				if (ViewUtils.confirm("「いいね！」")) {
					ls.like(schedule.getId());
				}
			}
		}
	}
}
