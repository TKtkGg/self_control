export type Task = {
    id: string;
    name: string;
    startTime: string;
    endTime: string;
    version: number;
}

export type DaySchedule = {
    dayOfWeek: number;
    title: string;
    tasks: Task[];
    version: number;
}