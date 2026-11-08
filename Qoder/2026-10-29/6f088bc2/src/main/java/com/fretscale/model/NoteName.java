package com.fretscale.model;

/**
 * A spelled note: a letter (0 = C … 6 = B) plus an accidental in semitones.
 * Keeping letter and accidental separate is what makes correct enharmonic
 * spelling possible (E-flat is not D-sharp in a scale context).
 */
public record NoteName(int letterIndex, int accidental) {

    public static NoteName natural(int letterIndex) {
        return new NoteName(letterIndex, 0);
    }

    public int pitchClass() {
        return PitchClass.normalize(PitchClass.naturalPitchClass(letterIndex) + accidental);
    }

    public String display() {
        String letter = PitchClass.LETTERS[Math.floorMod(letterIndex, 7)];
        if (accidental == 0) {
            return letter;
        }
        String mark = accidental > 0 ? "#" : "b";
        return letter + mark.repeat(Math.abs(accidental));
    }

    @Override
    public String toString() {
        return display();
    }
}
