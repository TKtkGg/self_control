"use client";

import {
    createContext,
    useCallback,
    useContext,
    useState,
    type ReactNode,
} from "react";

type AlarmRefreshContextValue = {
    refreshKey: number;
    refreshAlarm: () => void;
}

const AlarmRefreshContext = createContext<AlarmRefreshContextValue | null>(null);

export function AlarmProvider({ children }: { children: ReactNode }) {
    const [refreshKey, setRefreshKey] = useState(0);

    const refreshAlarm = useCallback(() => {
        setRefreshKey((current) => current + 1);
    }, []);

    return (
        <AlarmRefreshContext.Provider value={{ refreshKey, refreshAlarm }}>
            {children}
        </AlarmRefreshContext.Provider>
    )
}

export function useAlarmRefresh() {
    const context = useContext(AlarmRefreshContext);

    if (context === null) {
        throw new Error("useAlarmRefreshはAlarmProviderの内側で使ってください。");
    }

    return context;
}