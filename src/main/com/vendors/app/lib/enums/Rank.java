package com.vendors.app.lib.enums;

public enum Rank {
    COBRE("Cobre"), BRONCE("Bronce"), PLATA("Plata"), ORO("Oro");

    private String type;

    Rank(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
