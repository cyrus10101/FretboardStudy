package com.fretscale.model;

import java.util.List;

public final class Tuning {

    public static final int FRET_COUNT = 12;

    /** Standard tuning, high E on top. */
    public static final List<GuitarString> STANDARD = List.of(
            new GuitarString(0, "E", "High E", 4, "#A855F7"),
            new GuitarString(1, "B", "B", 11, "#EC4899"),
            new GuitarString(2, "G", "G", 7, "#22D3EE"),
            new GuitarString(3, "D", "D", 2, "#F59E0B"),
            new GuitarString(4, "A", "A", 9, "#EF4444"),
            new GuitarString(5, "E", "Low E", 4, "#3B82F6"));

    private Tuning() {
    }
}
