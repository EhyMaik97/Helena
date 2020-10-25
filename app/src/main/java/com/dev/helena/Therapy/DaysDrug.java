package com.dev.helena.Therapy;

import java.util.ArrayList;
import java.util.List;

public class DaysDrug {
    List<String> days;

    public DaysDrug(ArrayList<String> days)
    {
        this.days = days;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

}
