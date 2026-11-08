package com.fretscale.model;

/**
 * One string of the instrument. Index 0 is the physically highest string
 * (high E) so that rendering order matches the screen from top to bottom.
 */
public record GuitarString(int index, String label, String description,
                           int openPitchClass, String color) {

    public int pitchClassAt(int fret) {
        return PitchClass.normalize(openPitchClass + fret);
    }
}
