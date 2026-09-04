export type Task = {
    id: string;
    name: string;
    startTime: string;
    endTime: string;
}

export type DaySchedule = {
    dayOfWeek: number;
    title: string;
    tasks: Task[];
}