"use client";

import { use } from "react";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { apiGet } from "@/lib/apiClient";
import { toDayLabel } from "@/lib/dayOfWeek";
import { DaySchedule, Task } from "@/types/schedule";

type Props = {
    params: Promise<{ userId: string }>;
}

export default function UserSchedulePage({ params }: Props) {
    const router = useRouter();
    const resolvedParams = use(params);
    const { userId } = resolvedParams;
    const [days, setDays] = useState<DaySchedule[]>([]);
    const [selectedTask, setSelectedTask] = useState<Task | null>(null);

    useEffect(() => {
        apiGet(`/api/users/${userId}/schedule`).then((data) => {
            setDays(data.daySchedules ?? []);
        });

        const fetchUser = async () => {
            const response = await apiGet("/api/auth/user");
            if (!response.authenticated) {
                router.push("/");
            }
        }
        fetchUser();
    }, [userId, router]);

    return (
        <div>
            <h1>スケジュール</h1>

            {days.map((day) => (
                <div key={day.dayOfWeek}>
                    <h2>
                        {toDayLabel(day.dayOfWeek)}曜日: {day.title}
                    </h2>
                    <ul>
                        {day.tasks.map((task) => (
                            <li key={task.id} onClick={() => setSelectedTask(task)}>
                                {task.name} ({task.startTime} - {task.endTime})
                            </li>
                        ))}
                    </ul>
                </div>
            ))}

            {selectedTask && (
                <div>
                    <h3>タスク詳細</h3>
                    <p>名前: {selectedTask.name}</p>
                    <p>開始時間: {selectedTask.startTime}</p>
                    <p>終了時間: {selectedTask.endTime}</p>
                    <button onClick={() => setSelectedTask(null)}>閉じる</button>
                </div>
            )}
        </div>
    );
}
