package com.dev.helena.Therapy;


import com.google.firebase.database.Exclude;

public class Therapy {
    private String name;
    private int dosage;
    private String endTime, key;
    private String drugName;

    Therapy(){

    }

    public Therapy(String name, String drugName, int dosage, String endTime) {
        this.name = name;
        this.drugName = drugName;
        this.dosage = dosage;
        this.endTime = endTime;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Terapia: " + name + "  [ Medicinale: " + drugName + ", Dosaggio: " + dosage + ",Fine Terapia: " + endTime +"]";

    }

    public String getKey() {
        return key;
    }
}
