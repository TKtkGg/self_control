"use client";

import { useState } from "react";
import { apiPost } from "@/lib/apiClient";

export default function SignUpPage() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [passwordConfirm, setPasswordConfirm] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const handleSubmit = async () => {
        try {
            await apiPost("/api/auth/signup", {
                username,
                email,
                password,
                passwordConfirm,
            });
            setSuccess("ユーザー登録に成功しました");
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
            <h1>Sign Up</h1>
            <div>
                <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
                <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                <input type="password" placeholder="Confirm Password" value={passwordConfirm} onChange={(e) => setPasswordConfirm(e.target.value)} />
            </div>
            <button onClick={handleSubmit}>Sign Up</button>
            {error && <p>{error}</p>}
            {success && <p>{success}</p>}
        </div>
    );
}