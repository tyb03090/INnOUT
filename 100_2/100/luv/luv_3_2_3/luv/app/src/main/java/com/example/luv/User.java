package com.example.luv;

public class User {
    private String name;
    private String studentId;
    private boolean reservedLocker;

    public User() {
        // 기본 생성자
    }

    public User(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public String getStudentId() {
        return studentId;
    }

    public boolean isReservedLocker() {
        return reservedLocker;
    }
}
