package com.example.luv;

public class Lecture {
    private String day; // 요일
    private String startTime; // 시작 시간
    private String endTime; // 종료 시간
    private String lectureName; // 강의명
    private String professorName; // 교수명

    public Lecture(String day, String startTime, String endTime, String lectureName, String professorName) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lectureName = lectureName;
        this.professorName = professorName;
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

    public String getLectureName() {
        return lectureName;
    }

    public String getProfessorName() {
        return professorName;
    }
}
