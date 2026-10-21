"use client";

import { useEffect, useState } from "react";
import { apiGet, apiPatch } from "@/lib/apiClient";

export default function SettingPage() {
    const [isPublic, setIsPublic] = useState(false);
    const [isAuthorizeNotification, setIsAuthorizeNotification] = useState(false);
    const [version, setVersion] = useState<number>();

    useEffect(() => {
        apiGet("/api/settings").then((data) => {
            setIsPublic(data.isPublic);
            setIsAuthorizeNotification(data.isAuthorizeNotification);
            setVersion(data.version)
        });
    }, [])

    const handleSubmit = () => {
        if (version === undefined) {
            throw new Error("通信に失敗しました。");
        }
        
        apiPatch("/api/settings", { isPublic, isAuthorizeNotification, expectedVersion: version }).then((data) => {
            setIsPublic(data.isPublic);
            setIsAuthorizeNotification(data.isAuthorizeNotification);
            setVersion(data.version)
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