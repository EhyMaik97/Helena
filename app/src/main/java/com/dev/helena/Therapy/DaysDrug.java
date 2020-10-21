package com.dev.helena.Therapy;

import java.util.ArrayList;
import java.util.List;

public class DaysDrug {
    List<String> day;
    int id, therapyID;

    public DaysDrug(int id, List<String> day, int therapyID)
    {
        this.id = id;
        this.day = day;
        this.therapyID = therapyID;
    }

    public DaysDrug(ArrayList<String> day)
    {
        this.day = day;
    }

    public List<String> getDay() {
        return day;
    }

    public void setDay(List<String> day) {
        this.day = day;
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
