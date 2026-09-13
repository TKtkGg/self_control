"use client"

import { apiGet } from "@/lib/apiClient"
import { Task } from "@/types/schedule"
import { useEffect, useState } from "react"

export default function GlobalAlarm() {
    const [task, setTask] = useState<Task | null>(null);
    const [nextStartAt, setNextStartAt] = useState<string | null>(null);
    const [isStart, setIsStart] = useState<boolean>(false);

    useEffect(() => {
        const checkAlarm = async () => {
            try {
                const auth = await apiGet("/api/auth/status");
    
                if (!auth.authenticated) {
                    return;
                }
    
                const data = await apiGet("/api/alarm/next");
    
                setTask(data.task);
                setNextStartAt(data.nextStartAt);
            } catch (error) {
                console.error(error);
            }
        };
    
        checkAlarm();
    }, []);

    const refreshNextTask = async () => {
        const data = await apiGet("/api/alarm/next");
    
        setTask(data.task);
        setNextStartAt(data.nextStartAt);
        setIsStart(false);
    }

    useEffect(() => {
        if (!task || !nextStartAt) return;
    
        const target = new Date(nextStartAt);
        const delay = target.getTime() - Date.now();
    
        if (Number.isNaN(delay)) return;
    
        const timerId = window.setTimeout(() => {
            setIsStart(true);
            refreshNextTask();
        }, Math.max(0, delay));
    
        return () => {
            window.clearTimeout(timerId);
        };
    }, [task, nextStartAt]);


    return(
        <div>
            {task && isStart && (
                <div className="fixed top-0 left-0 right-0 z-50 bg-green-500">
                    タスクの開始時間です
                </div>
            )}
        </div>   
    )
}