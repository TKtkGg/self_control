"use client";

import { useEffect, useState } from "react";
import { apiGet, apiPatch } from "@/lib/apiClient";

export default function SettingPage() {
    const [isPublic, setIsPublic] = useState(false);
    const [isAuthorizeNotification, setIsAuthorizeNotification] = useState(false);

    useEffect(() => {
        apiGet("/api/setting").then((res) => {
            setIsPublic(res.isPublic);
            setIsAuthorizeNotification(res.isAuthorizeNotification);
        });
    }, [])

    const handleSubmit = () => {
        apiPatch("/api/setting", { isPublic, isAuthorizeNotification }).then((res) => {
            setIsPublic(res.isPublic);
            setIsAuthorizeNotification(res.isAuthorizeNotification);
        });
    };

    return (
        <div>
            <h1>Setting</h1>
            <select 
                value={isPublic ? "public" : "private"}
                onChange={(e) => setIsPublic(e.target.value === "public")}
            >
                <option value="public">公開</option>
                <option value="private">非公開</option>
            </select>
            <select
                value={isAuthorizeNotification ? "authorize" : "unauthorize"}
                onChange={(e) => setIsAuthorizeNotification(e.target.value === "authorize")}
            >
                <option value="authorize">許可</option>
                <option value="unauthorize">非許可</option>
            </select>
            <button onClick={handleSubmit}>Save</button>
        </div>
    );
}