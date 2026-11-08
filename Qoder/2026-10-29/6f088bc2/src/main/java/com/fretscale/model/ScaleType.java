package com.fretscale.model;

/**
 * Scale definitions expressed as scale degrees. Each degree carries both the
 * semitone distance from the tonic and the letter offset, so that spelling
 * follows music theory (a third is always a third letter, even when flat).
 */
public enum ScaleType {

    MAJOR("Major",
            new Degree[]{d(0, 0), d(1, 2), d(2, 4), d(3, 5), d(4, 7), d(5, 9), d(6, 11)},
            "The foundation of Western harmony. Whole–whole–half–whole–whole–whole–half steps "
                    + "produce the bright major sound and the I–IV–V chord family."),

    NATURAL_MINOR("Natural Minor",
            new Degree[]{d(0, 0), d(1, 2), d(2, 3), d(3, 5), d(4, 7), d(5, 8), d(6, 10)},
            "The relative minor of the major scale, built on its sixth degree. "
                    + "Lowered third, sixth and seventh give it a darker, melancholy colour."),

    MAJOR_PENTATONIC("Major Pentatonic",
            new Degree[]{d(0, 0), d(1, 2), d(2, 4), d(4, 7), d(5, 9)},
            "The major scale with its tension-bearing half steps (4 and 7) removed. "
                    + "Five notes that sit comfortably under the fingers over major progressions."),

    MINOR_PENTATONIC("Minor Pentatonic",
            new Degree[]{d(0, 0), d(2, 3), d(3, 5), d(4, 7), d(6, 10)},
            "The minor scale without its second and sixth degrees. The backbone of rock "
                    + "and blues lead playing, outlining the minor triad with no avoid notes."),

    BLUES("Blues",
            new Degree[]{d(0, 0), d(2, 3), d(3, 5), d(4, 6), d(4, 7), d(6, 10)},
            "Minor pentatonic plus the blue note (b5) — a tritone that adds grit and tension. "
                    + "Resolve the b5 up to the fifth or down to the fourth."),

    HARMONIC_MAJOR("Harmonic Major",
            new Degree[]{d(0, 0), d(1, 2), d(2, 4), d(3, 5), d(4, 7), d(5, 8), d(6, 11)},
            "A major scale with a lowered sixth. It pairs a bright major third with a dark b6, "
                    + "a favourite colour in jazz writing and film scoring."),

    MELODIC_MINOR("Melodic Minor (Ascending)",
            new Degree[]{d(0, 0), d(1, 2), d(2, 3), d(3, 5), d(4, 7), d(5, 9), d(6, 11)},
            "The jazz melodic minor: natural minor with a raised sixth and seventh. "
                    + "Ascending form only — the classical descending form is not applied here.");

    public record Degree(int letterOffset, int semitone) {
    }

    private final String displayName;
    private final Degree[] degrees;
    private final String explanation;

    ScaleType(String displayName, Degree[] degrees, String explanation) {
        this.displayName = displayName;
        this.degrees = degrees;
        this.explanation = explanation;
    }

    private static Degree d(int letterOffset, int semitone) {
        return new Degree(letterOffset, semitone);
    }

    public String displayName() {
        return displayName;
    }

    public Degree[] degrees() {
        return degrees.clone();
    }

    public String explanation() {
        return explanation;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
