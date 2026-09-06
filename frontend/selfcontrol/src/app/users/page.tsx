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
    const [username, setUsername] = useState("");
    const router = useRouter();

    useEffect(() => {
        apiGet("/api/users").then((data) => {
            setUsers(data.users);
        });
    }, []);

    const handleSearch = async () => {
        const params = new URLSearchParams({
            page: "0",
            size: "10"
        });

        const searchUsername = username.trim();

        if (searchUsername !== "") {
            params.set("username", searchUsername);
        }

        const data = await apiGet(`/api/users?${params.toString()}`);
        setUsers(data.users);
    }

    return (
        <div>
            <h1>ユーザー一覧</h1>
            <div>
                <label htmlFor="user-search">ユーザー名で検索</label>

                <input
                    id="user-search"
                    type="search"
                    placeholder="ユーザー名を入力"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    onKeyDown={(e) => {
                        if (e.key === "Enter") {
                            handleSearch();
                        }
                    }}
                />

                <button type="button" onClick={handleSearch}>
                    検索
                </button>
            </div>
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