package com.example.luv;

public class Locker {
    private int lockerNumber;
    private int fontNumber;
    private boolean reserved;

    public Locker(int lockerNumber, int fontNumber) {
        this.lockerNumber = lockerNumber;
        this.fontNumber = fontNumber;
        this.reserved = false; // 예약 상태 초기화
    }

    public int getLockerNumber() {
        return lockerNumber;
    }

    public int getFontNumber() {
        return fontNumber;
    }

    public boolean isReserved() {
        return reserved;
    }
}
