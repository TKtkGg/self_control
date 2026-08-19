const DAY_LABELS = ["月", "火", "水", "木", "金", "土", "日"];

export function toDayLabel(dayOfWeek: number): string {
    return DAY_LABELS[dayOfWeek];
}