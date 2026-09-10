"use client";

import { useState, useEffect } from "react";
import { apiGet } from "@/lib/apiClient";
import { toDayLabel } from "@/lib/dayOfWeek";
import { useRouter } from "next/navigation";
import { DaySchedule } from "@/types/schedule";
import { Task } from "@/types/schedule";

export default function HomePage() {
    const router = useRouter();

    useEffect(() => {
        const fetchUser = async () => {
            const response = await apiGet("/api/auth/user");
            if (!response.authenticated) {
                router.push("/");
            }
        }
        fetchUser();
    }, []);
    
    return (
        <div>
            <h1>Home</h1>
        
            <button onClick={() => router.push("/users")}>ユーザー一覧</button>
            <br />
            <button onClick={() => router.push("/setting/profile")}>プロフィール</button>
            <br />
            <button onClick={() => router.push("/setting")}>設定</button>

            {error && <div>{error}</div>}
        </div>
    );
}