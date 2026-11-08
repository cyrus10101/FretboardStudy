package com.fretscale.model;

import java.util.List;

/**
 * A selectable tonic together with its spelled name.
 */
public record Key(String name, NoteName tonic) {

    private static final List<String> CHROMATIC_NAMES = List.of(
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");

    public static List<String> chromaticNames() {
        return CHROMATIC_NAMES;
    }

    public static Key of(String name) {
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Key name must not be empty");
        }
        int letterIndex = PitchClass.letterIndex(trimmed.charAt(0));
        int accidental = 0;
        for (int i = 1; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '#') {
                accidental++;
            } else if (c == 'b' || c == '\u266d') {
                accidental--;
            } else {
                throw new IllegalArgumentException("Unsupported key name: " + name);
            }
        }
        return new Key(name, new NoteName(letterIndex, accidental));
    }

    public int tonicPitchClass() {
        return tonic.pitchClass();
    }

    public boolean prefersFlats() {
        return name.endsWith("b") || name.equals("F");
    }
}
