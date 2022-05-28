package main.java.com.vendors.models;

import main.java.com.vendors.enums.Rank;

import java.util.HashMap;
import java.util.Map;

public class Vendor {
    private final long cedula;
    private String name;
    private Rank previousRank;
    private Rank currentRank;
    private double salesMonthly;
    private final Map<String, Double> commission;

    public Vendor(long cedula, String name, Rank currentRank, double salesMonthly) {
        this.cedula = cedula;
        this.name = name;
        this.currentRank = currentRank;
        this.salesMonthly = salesMonthly;
        this.commission = new HashMap<>();
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

    public double getSalesMonthly() {
        return salesMonthly;
    }

    public void setSalesMonthly(double salesMonthly) {
        this.salesMonthly = salesMonthly;
    }

    public double getCommission() {
        double commissionValue = calculateCommission();

        return commissionValue;
    }

    public void setCommissionType(String type, double percentage) {
        commission.put(type, percentage);
    }

    public double getCommissionType(String type) {
        return commission.get(type);
    }

    public String getCommissionDescription() {
        String description = "";
        String separator = "";

        for (Map.Entry<String, Double> entry : commission.entrySet()) {
            description += separator + entry.getValue() * 100 + "% " + entry.getKey();
            separator = " + ";
        }

        return description;
    }

    public double calculateCommission() {
        double accumulator = 0;

        for (Map.Entry<String, Double> entry : commission.entrySet()) {
            accumulator += salesMonthly * entry.getValue();
        }

        return accumulator;
    }

    public void assignPersonalCommission() {
        String type = "personal";
        double percentage = 0;

        switch (currentRank) {
            case COBRE:
                percentage = 10;
                break;
            case BRONCE:
                percentage = 15;
                break;
            case PLATA:
                percentage = 20;
                break;
            case ORO:
                percentage = 25;
                break;
        }

        percentage = percentage / 100;

        setCommissionType(type, percentage);
    }

    public void assignLevelUpCommission() {
        String type = "level up";
        double percentage = 0;

        if (previousRank == Rank.COBRE && currentRank == Rank.BRONCE) {
            percentage = 5;
        }

        if (previousRank == Rank.BRONCE && currentRank == Rank.PLATA) {
            percentage = 10;
        }

        if (previousRank == Rank.PLATA && currentRank == Rank.ORO) {
            percentage = 15;
        }

        percentage = percentage / 100;

        setCommissionType(type, percentage);
    }
}
