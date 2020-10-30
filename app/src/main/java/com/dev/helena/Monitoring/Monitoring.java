package com.dev.helena.Monitoring;

public class Monitoring {

    private String nameMonitoring;
    private String date;
    private String pathImage;
    private String key;

    Monitoring()
    {

    }

    public Monitoring(String name, String date, String path)
    {
        this.nameMonitoring = name;
        this.date = date;
        this.pathImage = path;
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

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
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
