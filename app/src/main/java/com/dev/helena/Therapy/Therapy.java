package com.dev.helena.Therapy;


public class Therapy {
    private int id;
    private String name;
    private int dosage;
    private String endTime;
    private String drugName;

    public Therapy(int id, String name, String drugName, int dosage, String endTime) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    @Override
    public String toString() {
        return "Terapia: " + name + "  [ Medicinale: " + drugName + ", Dosaggio: " + dosage + ",Fine Terapia: " + endTime +"]";

    }
}
