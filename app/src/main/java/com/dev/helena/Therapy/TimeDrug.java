package com.dev.helena.Therapy;

public class TimeDrug {
    int hour, minute, id, therapyID;

    public TimeDrug(int id, int hour, int minute, int therapyID)
    {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.therapyID = therapyID;
    }

    public TimeDrug(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTherapyID() {
        return therapyID;
    }

    public void setTherapyID(int therapyID) {
        this.therapyID = therapyID;
    }
}
