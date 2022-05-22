package main.java.com.vendors.models;

import main.java.com.vendors.enums.Rank;

public class Vendor {
    private final long cedula;
    private String name;
    private Rank previousRank;
    private Rank currentRank;
    private double comission;
    private String comissionDescription;

    public Vendor(long cedula, String name, Rank currentRank, double comission, String comissionDescription) {
        this.cedula = cedula;
        this.name = name;
        this.currentRank = currentRank;
        this.comission = comission;
        this.comissionDescription = comissionDescription;
    }

    public long getCedula() {
        return cedula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rank getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(Rank currentRank) {
        this.previousRank = this.currentRank;
        this.currentRank = currentRank;
    }

    public Rank getPreviousRank() {
        return previousRank;
    }

    public void setPreviousRank(Rank previousRank) {
        this.previousRank = previousRank;
    }

    public double getComission() {
        return comission;
    }

    public void setComission(double comission) {
        this.comission = comission;
    }

    public String getComissionDescription() {
        return comissionDescription;
    }

    public void setComissionDescription(String comissionDescription) {
        this.comissionDescription = comissionDescription;
    }
}
