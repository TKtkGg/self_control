"use client";

import { useState, useEffect } from "react";
import { apiGet } from "@/lib/apiClient";
import { toDayLabel } from "@/lib/dayOfWeek";
import { useRouter } from "next/navigation";
import { DaySchedule } from "@/types/schedule";

export default function HomePage() {
    const [day, setDay] = useState<DaySchedule>();
    const router = useRouter();

    useEffect(() => {
        const d = new Date();
        const customDay = (d.getDay() + 6) % 7;

        apiGet(`/api/schedule/${customDay}`).then((data) => {
            setDay(data)
        })

        const fetchUser = async () => {
            const response = await apiGet("/api/auth/user");
            if (!response.authenticated) {
                router.push("/");
            }
        }
        fetchUser();
    }, [router]);
    
    return (
        <div>
            <h1>Home</h1>
            {day && (
                <div key={day.dayOfWeek}>
                    <h2>今日の予定</h2>
                    <h2>
                        {toDayLabel(day.dayOfWeek)}曜日: {day.title}
                    </h2>
                    <ul>
                        {day.tasks.map((task) => (
                            <li key={task.id}>
                                {task.name} ({task.startTime} - {task.endTime})
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            <button onClick={() => router.push("/schedule")}>スケジュール</button>
            <br />
            <button onClick={() => router.push("/users")}>ユーザー一覧</button>
            <br />
            <button onClick={() => router.push("/setting/profile")}>プロフィール</button>
            <br />
            <button onClick={() => router.push("/setting")}>設定</button>
        </div>
    );
}