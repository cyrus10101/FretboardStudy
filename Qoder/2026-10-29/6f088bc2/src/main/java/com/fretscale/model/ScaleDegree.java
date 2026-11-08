package com.fretscale.model;

public record ScaleDegree(int pitchClass, NoteName name, String intervalLabel, int semitone) {

    @Override
    public String toString() {
        return name.display() + " (" + intervalLabel + ")";
    }
}
