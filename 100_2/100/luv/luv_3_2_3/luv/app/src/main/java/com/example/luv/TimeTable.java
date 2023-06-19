package com.example.luv;

import java.util.ArrayList;

public class TimeTable {
    private ArrayList<Lecture> lectures;

    public TimeTable() {
        lectures = new ArrayList<>();
    }

    public void addLecture(Lecture lecture) {
        lectures.add(lecture);
    }

    public ArrayList<Lecture> getLectures() {
        return lectures;
    }
}
