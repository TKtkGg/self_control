"use client";

import { use } from "react";

type Props = {
    params: Promise<{ userId: string }>;
}

export default function UserPage({ params }: Props) {
    const resolvedParams = use(params);
    const { userId } = resolvedParams;
    return (
        <div>
            <h1>ユーザー詳細</h1>
            <p>{userId}</p>
        </div>
    );
}