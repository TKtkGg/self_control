"use client";

import { use } from "react";
import { apiGet, apiPost, apiDelete } from "@/lib/apiClient";
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
    const [likeCount, setLikeCount] = useState<number>(0);
    const [isLiked, setIsLiked] = useState<boolean | null>(null);

    useEffect(() => {
        apiGet(`/api/users/${userId}/profile`).then((data) => {
            setUsername(data.username);
            setSelfIntroduce(data.selfIntroduce);
            const src = data.icon ? `data:image/jpeg;base64,${data.icon}` : undefined;
            setIconSrc(src);
            setLikeCount(data.likeCount);
            setIsLiked(data.isLiked);
        });
    }, [userId]);

    const handleLike = () => {
        apiPost(`/api/users/${userId}/like`, {}).then((data) => {
            setLikeCount(data.likeCount);
            setIsLiked(data.isLiked);
        });
    }

    const handleUnlike = () => {
        apiDelete(`/api/users/${userId}/like`).then((data) => {
            setLikeCount(data.likeCount);
            setIsLiked(data.isLiked);
        });
    }

    return (
        <div>
            <h1>ユーザー詳細</h1>
            <p>{username}</p>
            <p>{selfIntroduce}</p>
            <p>いいね数：{likeCount}</p>
            {isLiked === null || isLiked === false ? (
                <button onClick={handleLike}>いいね</button>
            ) : (
                <button onClick={handleUnlike}>いいね解除</button>
            )}
            <img src={iconSrc} alt="icon" />
        </div>
    );
}