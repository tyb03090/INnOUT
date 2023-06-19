package com.example.luv;

public class Course {
    private String day;
    private String startTime;
    private String endTime;

    public Course(String day, String startTime, String endTime, String selectedCourse, String selectedProfessor, String selectedClassroom) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
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
}
