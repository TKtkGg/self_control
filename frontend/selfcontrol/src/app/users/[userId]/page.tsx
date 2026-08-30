"use client";

import { use } from "react";
import { apiGet } from "@/lib/apiClient";
import { useState, useEffect } from "react";

type Props = {
    params: Promise<{ userId: string }>;
}

export default function UserPage({ params }: Props) {
    const resolvedParams = use(params);
    const { userId } = resolvedParams;

    const [username, setUsername] = useState<string>("");
    const [selfIntroduce, setSelfIntroduce] = useState<string>("");
    const [iconSrc, setIconSrc] = useState<string | undefined>(undefined);

    useEffect(() => {
        apiGet(`/api/setting/profile/${userId}`).then((data) => {
            setUsername(data.username);
            setSelfIntroduce(data.selfIntroduce);
            const src = data.icon ? `data:image/jpeg;base64,${data.icon}` : undefined;
            setIconSrc(src);
        });
    }, [userId]);

    return (
        <div>
            <h1>ユーザー詳細</h1>
            <p>{username}</p>
            <p>{selfIntroduce}</p>
            <img src={iconSrc} alt="icon" />
        </div>
    );
}