const handleError = async (response: Response): Promise<never> => {
    const error = await response.json().catch(() => null) as {
        detail?: unknown;
        message?: unknown;
        errors?: unknown;
    } | null;

    const detail = typeof error?.detail === "string"
        ? error.detail
        : typeof error?.message === "string"
            ? error.message
            : null;

    const validationMessages = Array.isArray(error?.errors)
        ? error.errors
            .map((item) => {
                if (typeof item !== "object" || item === null) return null;
                const field = "field" in item && typeof item.field === "string" ? item.field : null;
                const message = "message" in item && typeof item.message === "string" ? item.message : null;
                return field && message ? `${field}: ${message}` : message;
            })
            .filter((message): message is string => Boolean(message))
        : [];

    if (detail && validationMessages.length > 0) {
        throw new Error(`${detail} (${validationMessages.join(", ")})`);
    }
    if (detail) {
        throw new Error(detail);
    }
    if (validationMessages.length > 0) {
        throw new Error(validationMessages.join(", "));
    }

    throw new Error("通信に失敗しました。");
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
