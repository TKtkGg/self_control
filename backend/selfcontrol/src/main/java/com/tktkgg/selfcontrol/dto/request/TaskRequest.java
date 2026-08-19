package com.tktkgg.selfcontrol.dto.request;

public class TaskRequest {
    private int dayOfWeek;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String name;

    public TaskRequest(int dayOfWeek, int startHour, int startMinute, int endHour, int endMinute, String name) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.name = name;
    }

    public TaskRequest() {}

    public int getDayOfWeek() {
        return dayOfWeek;
    }
    
    public int getStartHour() {
        return startHour;
    }
    
    public int getStartMinute() {
        return startMinute;
    }
    
    public int getEndHour() {
        return endHour;
    }
    
    public int getEndMinute() {
        return endMinute;
    }
    
    public String getName() {
        return name;
    }
    
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    
    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }
    
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}