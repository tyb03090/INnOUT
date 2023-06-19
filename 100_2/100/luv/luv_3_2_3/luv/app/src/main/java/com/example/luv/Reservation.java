package com.example.luv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Reservation {
    private String endDate;
    private int lockerNumber;
    private String name;
    private String startDate;
    private String studentId;
    private int fontNumber;

    public Reservation() {
        // 기본 생성자 필요
    }
    public Reservation(String studentId, String name, int lockerNumber, int fontNumber, String startDate, String endDate) {
        this.studentId = studentId;
        this.name = name;
        this.lockerNumber = lockerNumber;
        this.fontNumber = fontNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // getter와 setter 메서드
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLockerNumber() {
        return lockerNumber;
    }

    public void setLockerNumber(int lockerNumber) {
        this.lockerNumber = lockerNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getFontNumber() {
        return fontNumber;
    }

    public void setFontNumber(int fontNumber) {
        this.fontNumber = fontNumber;
    }

    public boolean isExpired() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date endDateTime = sdf.parse(endDate);
            Date currentDateTime = new Date();

            return currentDateTime.after(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExpirationApproaching() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date endDateTime = sdf.parse(endDate);
            Date currentDateTime = new Date();

            long timeDifference = endDateTime.getTime() - currentDateTime.getTime();
            long minutesRemaining = timeDifference / (60 * 1000);

            // 종료일이 30분 이내인 경우 다가오는 종료일로 간주합니다.
            return minutesRemaining <= 30;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}

