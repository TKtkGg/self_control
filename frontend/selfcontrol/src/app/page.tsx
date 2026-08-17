"use client";

import { useRouter } from "next/navigation";
import { apiPost, apiGet } from "@/lib/apiClient";
import { useState, useEffect } from "react";

export default function HomePage() {
  const router = useRouter();
  const [authenticated, setAuthenticated] = useState(false);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchUser = async () => {
      const response = await apiGet("/api/auth/user");
      if (response.authenticated) {
        setAuthenticated(true);
      } else {
        setAuthenticated(false);
      }
    }
    fetchUser();
  }, [])

  const handleLogout = async () => {
    try {
      await apiPost("/api/auth/logout");
      setSuccess("ログアウトに成功しました");
      setAuthenticated(false);
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
      <h1>Home Page</h1>
      <button onClick={() => router.push("/signup")}>Sign Up</button>
      <button onClick={() => router.push("/login")}>Login</button>
      <button onClick={handleLogout}>Logout</button>
      {authenticated && <p>ログイン中</p>}
      {success && <p>{success}</p>}
      {error && <p>{error}</p>}
    </div>
  );
}
