"use client";

import { useEffect, useState } from "react";
import { apiGet, apiPatch } from "@/lib/apiClient";
import { useAlarmRefresh } from "@/components/AlarmProvider";

export default function SettingPage() {
    const [isPublic, setIsPublic] = useState(false);
    const [isAuthorizeNotification, setIsAuthorizeNotification] = useState(false);
    const [notificationMessage, setNotificationMessage] = useState("");
    const [version, setVersion] = useState<number>();
    const { refreshAlarm } = useAlarmRefresh();

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
        })
        .then(() => {
            refreshAlarm();
        });
    };

    const handleNotificationSettingChange = async (shouldEnable: boolean) => {
        setNotificationMessage("");

        if (!shouldEnable) {
            setIsAuthorizeNotification(false);
            return;
        }

        if (!("Notification" in window) || !window.isSecureContext) {
            setIsAuthorizeNotification(false);
            setNotificationMessage("このブラウザでは通知を利用できません。");
            return;
        }

        let permission = Notification.permission;

        if (permission === "default") {
            try {
                permission = await Notification.requestPermission();
            } catch {
                permission = "denied";
                setIsAuthorizeNotification(false);
                setNotificationMessage("通知許可の取得に失敗しました。")
                return;
            }
        }

        const allowed = permission === "granted";
        setIsAuthorizeNotification(allowed);

        if (permission === "denied") {
            setNotificationMessage("ブラウザで通知が拒否されています。ブラウザのサイト設定を確認してください。");
        } else if (!allowed) {
            setNotificationMessage("通知が許可されませんでした。");
        }

    }

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
                onChange={(e) => handleNotificationSettingChange(e.target.value === "authorize")}
            >
                <option value="authorize">許可</option>
                <option value="unauthorize">非許可</option>
            </select>
            <button onClick={handleSubmit}>Save</button>
            <p>{notificationMessage}</p>
        </div>
    );
}