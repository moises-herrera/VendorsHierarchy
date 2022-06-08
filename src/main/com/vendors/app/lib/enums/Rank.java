package com.vendors.app.lib.enums;

public enum Rank {
    COBRE("Cobre", 0), BRONCE("Bronce", 1), PLATA("Plata", 2), ORO("Oro", 3);

    private String type;
    private int value;

    Rank(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public int getValue() {
        return this.value;
    }
}
