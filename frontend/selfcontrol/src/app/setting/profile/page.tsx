"use client";

import { useState, useEffect } from "react";
import { apiGet, apiPatch } from "@/lib/apiClient";

export default function ProfilePage() {
    const [username, setUsername] = useState<string>("");
    const [selfIntroduce, setSelfIntroduce] = useState<string>("");
    const [icon, setIcon] = useState<string>("");
    const [iconSrc, setIconSrc] = useState<string | undefined>(undefined);
    const [profileVersion, setProfileVersion] = useState<number>();
    const [userVersion, setUserVersion] = useState<number>();

    useEffect(() => {
        apiGet(`/api/profile`).then((data) => {
            setUsername(data.username);
            setSelfIntroduce(data.selfIntroduce);
            setIcon(data.icon ?? "");
            const src = data.icon ? `data:image/jpeg;base64,${data.icon}` : undefined;
            setIconSrc(src);
            setProfileVersion(data.profileVersion);
            setUserVersion(data.userVersion);
        });
    }, []);

    const handleUpdate = () => {
        if (profileVersion === undefined || userVersion === undefined) {
            throw new Error("通信に失敗しました。");
        }

        apiPatch(`/api/profile`, { 
            username, 
            selfIntroduce, 
            icon, 
            expectedProfileVersion: profileVersion, 
            expectedUserVersion: userVersion 
        }).then((data) => {
            setUsername(data.username);
            setSelfIntroduce(data.selfIntroduce);
            setIcon(data.icon ?? "");
            const src = data.icon ? `data:image/jpeg;base64,${data.icon}` : undefined;
            setIconSrc(src);
            setProfileVersion(data.profileVersion);
            setUserVersion(data.userVersion);
        });
    }


    return (
        <div>
            <h1>Profile</h1>
            <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
            <input type="text" value={selfIntroduce} onChange={(e) => setSelfIntroduce(e.target.value)} />
            <input 
                type="file" 
                accept="image/*" 
                onChange={(e) => {
                    const files = e.target.files?.[0];
                    if (!files) return;

                    const reader = new FileReader();
                    reader.onload = () => {
                        const dataUrl = reader.result as string;
                        const base64 = dataUrl.split(",")[1];
                        setIcon(base64);
                        setIconSrc(dataUrl);
                    }
                    reader.readAsDataURL(files);
                }} 
            />
            {iconSrc && <img src={iconSrc} alt="icon preview"/>}

            <button onClick={handleUpdate}>Update</button>
        </div>
    );
}