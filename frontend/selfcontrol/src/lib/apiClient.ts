const handleError = async (response: Response) => {
    const error = await response.json().catch(() => null);
    let message = `HTTP ${response.status}`;

    if(typeof error?.message === 'string') {
        message = error.message;
        throw new Error(message);
    } else {
        throw new Error("通信に失敗しました。");
    }
}

export const apiGet = async(path: string) => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${path}`, {
        method: "GET",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
    });
    if(!response.ok) {
        await handleError(response);
    }
    return response.json();
}

export const apiPost = async(path: string, body?: Record<string, unknown>) => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${path}`, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
        body: body ? JSON.stringify(body) : undefined,
    });
    if(!response.ok) {
        await handleError(response);
    }
    return response.json();
}

export const apiPatch = async(path: string, body?: Record<string, unknown>) => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${path}`, {
        method: "PATCH",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
        body: body ? JSON.stringify(body) : undefined,
    });

    if(!response.ok) {
        await handleError(response);
    }
    return response.json();
}

export const apiDelete = async(path: string) => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${path}`, {
        method: "DELETE",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
    });

    if(!response.ok) {
        await handleError(response);
    }
    return response.json();
}