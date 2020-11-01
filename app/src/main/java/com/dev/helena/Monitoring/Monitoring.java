package com.dev.helena.Monitoring;

import java.util.ArrayList;
import java.util.List;

public class Monitoring {

    private String nameMonitoring;
    private String date;
    private String key;

    private ArrayList<String> path;

    Monitoring()
    {

    }

    public Monitoring(String name, String date, ArrayList<String> path)
    {
        this.nameMonitoring = name;
        this.date = date;
        this.path = path;
    }


    public ArrayList<String> getPath() {
        return path;
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
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

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
