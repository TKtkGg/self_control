"use client";

import { apiGet } from "@/lib/apiClient";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

type Users = {
    id: string;
    name: string;
}

export default function UsersPage() {
    const [users, setUsers] = useState<Users[]>([]);
    const router = useRouter();

    useEffect(() => {
        apiGet("/api/users").then((data) => {
            setUsers(data.users);
        });
    }, []);

    return (
        <div>
            <h1>ユーザー一覧</h1>
            <ul>
                {users.map((user) => (
                    <div key={user.id}>
                        <button key={user.id} onClick={() => router.push(`/users/${user.id}`)}>{user.name}</button>
                        <br />
                    </div>
                    
                ))}
            </ul>
        </div>
    );
}