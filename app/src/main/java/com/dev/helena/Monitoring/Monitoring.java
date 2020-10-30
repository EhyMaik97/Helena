package com.dev.helena.Monitoring;

public class Monitoring {

    private String nameMonitoring, date;

    Monitoring()
    {

    }

    public Monitoring(String name, String date)
    {
        this.nameMonitoring = name;
        this.date = date;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNameMonitoring() {
        return nameMonitoring;
    }

    public void setNameMonitoring(String nameMonitoring) {
        this.nameMonitoring = nameMonitoring;
    }

    @Override
    public String toString() {
        return "Nome referto: " + nameMonitoring + '\'' + ", Data referto= " + date + '\'' + '}';
    }
}
