package com.example.luv;

import java.util.Objects;

public class TimeSlot {
    private String day;
    private String startTime;
    private String endTime;
    private String course;
    private String professor;
    private String classroom;
    private  String time;

    public TimeSlot(String day, String startTime, String endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCourse() {
        return course;
    }

    public String getProfessor() {
        return professor;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getTime() {
        return time;
    }
}

