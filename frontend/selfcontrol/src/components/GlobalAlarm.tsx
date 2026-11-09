"use client"

import { apiGet } from "@/lib/apiClient"
import { Task } from "@/types/schedule"
import { useAlarmRefresh } from "@/components/AlarmProvider"
import { useCallback, useEffect, useState } from "react"

export default function GlobalAlarm() {
    const [task, setTask] = useState<Task | null>(null);
    const [nextStartAt, setNextStartAt] = useState<string | null>(null);
    const [notificationTask, setNotificationTask] = useState<Task | null>(null);
    const { refreshKey } = useAlarmRefresh();

    useEffect(() => {
        let cancelled = false;

        const checkAlarm = async () => {
            try {
                const auth = await apiGet("/api/auth/status");

                if (cancelled) return;
    
                if (!auth.authenticated) {
                    setTask(null);
                    setNextStartAt(null);
                    setNotificationTask(null);
                    return;
                }
    
                const data = await apiGet("/api/alarm/next");

                if (cancelled) return
    
                setTask(data.task);
                setNextStartAt(data.nextStartAt);
            } catch (error) {
                if (!cancelled) {
                    console.error(error);
                }
            }
        };
    
        void checkAlarm();

        return () => {
            cancelled = true;
        };
    }, [refreshKey]);

    const refreshNextTask = useCallback(async () => {
        const data = await apiGet("/api/alarm/next");
    
        setTask(data.task);
        setNextStartAt(data.nextStartAt);
    }, [])

    useEffect(() => {
        if (!task || !nextStartAt) return;
    
        const target = new Date(nextStartAt);
        const delay = target.getTime() - Date.now();
    
        if (Number.isNaN(delay)) return;

        const timerId = window.setTimeout(() => {
            setNotificationTask(task);
            void refreshNextTask().catch((error) => console.error(error));
        }, Math.max(0, delay));
    
        return () => {
            window.clearTimeout(timerId);
        };
    }, [task, nextStartAt, refreshNextTask]);

    useEffect(() => {
        if (!notificationTask) return;
    
        const timerId = window.setTimeout(() => {
            setNotificationTask(null);
        }, 5000);
    
        return () => {
            window.clearTimeout(timerId);
        };
    }, [notificationTask]);


    return(
        <div>
            {notificationTask && (
                <div className="fixed top-0 left-0 right-0 z-50 bg-green-500">
                    タスク「{notificationTask.name}」の開始時間です
                </div>
            )}
        </div>   
    )
}