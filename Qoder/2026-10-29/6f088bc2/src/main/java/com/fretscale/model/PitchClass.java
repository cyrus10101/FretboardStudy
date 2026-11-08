package com.fretscale.model;

/**
 * Static helpers for working with pitch classes (0 = C … 11 = B).
 */
public final class PitchClass {

    public static final String[] LETTERS = {"C", "D", "E", "F", "G", "A", "B"};

    private static final String[] SHARP_NAMES =
            {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final String[] FLAT_NAMES =
            {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};

    /** Pitch classes of the natural letters C D E F G A B. */
    private static final int[] NATURAL_PITCH_CLASSES = {0, 2, 4, 5, 7, 9, 11};

    private PitchClass() {
    }

    public static int normalize(int pitchClass) {
        return Math.floorMod(pitchClass, 12);
    }

    public static int naturalPitchClass(int letterIndex) {
        return NATURAL_PITCH_CLASSES[Math.floorMod(letterIndex, 7)];
    }

    public static int letterIndex(char letter) {
        for (int i = 0; i < LETTERS.length; i++) {
            if (LETTERS[i].charAt(0) == Character.toUpperCase(letter)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown note letter: " + letter);
    }

    public static String chromaticName(int pitchClass, boolean preferFlats) {
        return (preferFlats ? FLAT_NAMES : SHARP_NAMES)[normalize(pitchClass)];
    }

    public static String chromaticName(int pitchClass) {
        return chromaticName(pitchClass, false);
    }
}
