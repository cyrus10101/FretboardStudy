package com.fretscale.model;

/**
 * One playable position on the fretboard with everything the UI needs to
 * render it. Fret 0 is the open string.
 */
public record FretboardNote(GuitarString string, int fret, int pitchClass,
                            String noteLabel, String intervalLabel,
                            boolean inScale, boolean isTonic, boolean inTriad) {

    public String tooltipText() {
        return "Note: " + noteLabel + "\nString: " + string.label() + "\nFret: " + fret;
    }

    public String positionText() {
        return string.description() + " string · fret " + fret;
    }
}
