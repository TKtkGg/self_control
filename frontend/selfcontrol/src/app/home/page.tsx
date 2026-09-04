"use client";

import { useState, useEffect } from "react";
import { apiGet, apiPost, apiPatch, apiDelete } from "@/lib/apiClient";
import { toDayLabel } from "@/lib/dayOfWeek";
import { useRouter } from "next/navigation";
import { DaySchedule } from "@/types/schedule";
import { Task } from "@/types/schedule";

export default function HomePage() {
    const [days, setDays] = useState<DaySchedule[]>([]);
    const [id, setId] = useState<string>("");
    const [dayOfWeek, setDayOfWeek] = useState<number>(0);
    const [taskName, setTaskName] = useState<string>("");
    const [startTime, setStartTime] = useState<string>("");
    const [endTime, setEndTime] = useState<string>("");
    const [openUpdateModal, setOpenUpdateModal] = useState<boolean>(false);
    const [openCreateModal, setOpenCreateModal] = useState<boolean>(false);
    const [error, setError] = useState<string>("");
    const router = useRouter();

    useEffect(() => {
        apiGet("/api/home").then((data) => {
            setDays(data.dayScheduleResponses ?? []);
        });

        const fetchUser = async () => {
            const response = await apiGet("/api/auth/user");
            if (!response.authenticated) {
                router.push("/");
            }
        }
        fetchUser();
    }, []);

    const refreshHome = () => {
        apiGet("/api/home").then((data) => {
            setDays(data.dayScheduleResponses ?? []);
        });
    }

    const splitTime = (time: string) => {
        const [hour, minute] = time.split(":");
        return {
            hour: Number(hour),
            minute: Number(minute),
        }
    }

    const onClickTask = (task: Task) => {
        setOpenUpdateModal(true);
        setId(task.id);
        setTaskName(task.name);
        setStartTime(task.startTime.slice(0, 5));
        setEndTime(task.endTime.slice(0, 5));
    }

    const onClickCreate = (dayOfWeek: number) => {
        setOpenCreateModal(true);
        setDayOfWeek(dayOfWeek);
        setId("");
        setTaskName("");
        setStartTime("00:00");
        setEndTime("00:00");
    }

    const onUpdateTitle = (dayOfWeek: number, title: string) => {
        apiPatch(`/api/home/schedule/${dayOfWeek}`, {
            title: title,
        })
        .catch((error) => setError(error.message))
        .finally(() => refreshHome());
    }

    const onCreateTask = (dayOfWeek: number, taskName: string, startTime: string, endTime: string) => {
        setOpenCreateModal(false);

        const start = splitTime(startTime);
        const end = splitTime(endTime);

        apiPost(`/api/home/task`, {
            dayOfWeek: dayOfWeek,
            name: taskName,
            startHour: start.hour,
            startMinute: start.minute,
            endHour: end.hour,
            endMinute: end.minute,
        })
        .catch((error) => setError(error.message))
        .finally(() => refreshHome());
    }

    const onUpdateTask = (id: string, taskName: string, startTime: string, endTime: string) => {
        setOpenUpdateModal(false);
        setId(id);

        const start = splitTime(startTime);
        const end = splitTime(endTime);

        apiPatch(`/api/home/task/${id}`, {
            name: taskName,
            startHour: start.hour,
            startMinute: start.minute,
            endHour: end.hour,
            endMinute: end.minute,
        })
        .catch((error) => setError(error.message))
        .finally(() => refreshHome());
    }

    const onDeleteTask = (id: string) => {
        setOpenUpdateModal(false);
        setId(id);
        apiDelete(`/api/home/task/${id}`)
        .catch((error) => setError(error.message))
        .finally(() => refreshHome());
    }
    
    return (
        <div>
            <h1>Home</h1>

            {days.map((day) => (
                <div key={day.dayOfWeek}>
                    <h2>
                        {toDayLabel(day.dayOfWeek)}曜日: 
                        <input
                            type="text" 
                            defaultValue={day.title} 
                            onBlur={(e) => {
                                const next = e.target.value;
                                if (next === day.title) return;
                                onUpdateTitle(day.dayOfWeek, next);
                            }}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") {
                                    e.currentTarget.blur();
                                }
                            }}
                        />
                    </h2>
                    <ul>
                        {day.tasks.map((task) => (
                            <li key={task.id} onClick={() => onClickTask(task)}>
                                {task.name} ({task.startTime} - {task.endTime})
                            </li>
                        ))}
                    </ul>
                    <button onClick={() => onClickCreate(day.dayOfWeek)}>作成</button>
                </div>
            ))}

        {openUpdateModal && (
            <div>
                <h3>タスク編集</h3>
                <input type="text" value={taskName} onChange={(e) => setTaskName(e.target.value)} />
                <input type="time" value={startTime} onChange={(e) => setStartTime(e.target.value)} />
                <input type="time" value={endTime} onChange={(e) => setEndTime(e.target.value)} />
                <button onClick={() => onUpdateTask(id, taskName, startTime, endTime)}>更新</button>
                <button onClick={() => onDeleteTask(id)}>削除</button>
            </div>
        )}

        {openCreateModal && (
            <div>
                <h3>タスク作成</h3>
                <input type="text" value={taskName} onChange={(e) => setTaskName(e.target.value)} />
                <input type="time" value={startTime} onChange={(e) => setStartTime(e.target.value)} />
                <input type="time" value={endTime} onChange={(e) => setEndTime(e.target.value)} />
                <button onClick={() => onCreateTask(dayOfWeek, taskName, startTime, endTime)}>作成</button>
            </div>
        )}
        
        <button onClick={() => router.push("/users")}>ユーザー一覧</button>
        <br />
        <button onClick={() => router.push("/setting/profile")}>プロフィール</button>

        {error && <div>{error}</div>}
        </div>
    );
}