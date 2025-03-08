package com.example.tp1appmobil;

public class Module {
    private String name;
    private int coefficient;
    private double tdScore;
    private double tpScore;
    private double examScore;

    // Constructor
    public Module(String name, int coefficient) {
        this.name = name;
        this.coefficient = coefficient;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public double getTdScore() {
        return tdScore;
    }

    public void setTdScore(double tdScore) {
        this.tdScore = tdScore;
    }

    public double getTpScore() {
        return tpScore;
    }

    public void setTpScore(double tpScore) {
        this.tpScore = tpScore;
    }

    public double getExamScore() {
        return examScore;
    }

    public void setExamScore(double examScore) {
        this.examScore = examScore;
    }

    // Calculate module average
    public double calculateModuleAverage() {
        return ((tdScore + tpScore) / 2 + examScore) / 2;
    }
}