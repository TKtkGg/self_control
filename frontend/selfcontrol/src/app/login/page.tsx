"use client";

import { useState } from "react";
import { apiPost } from "@/lib/apiClient";

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const handleSubmit = async () => {
        try {
            await apiPost("/api/auth/login", {
                email,
                password,
            });
            setSuccess("ログインに成功しました");
        } catch (error: unknown) {
            if(error instanceof Error) {
                setError(error.message);
            } else {
                setError("通信に失敗しました");
            }
        }
    }

    return (
        <div>
            <h1>Login</h1>
            <div>
                <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
            </div>
            <button onClick={handleSubmit}>Login</button>
            {error && <p>{error}</p>}
            {success && <p>{success}</p>}
        </div>
    );
}