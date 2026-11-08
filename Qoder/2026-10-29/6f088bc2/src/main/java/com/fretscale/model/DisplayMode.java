package com.fretscale.model;

public enum DisplayMode {

    NOTES("Notes"),
    INTERVALS("Intervals");

    private final String displayName;

    DisplayMode(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
