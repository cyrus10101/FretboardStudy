package com.fretscale.theory;

import com.fretscale.model.NoteName;
import com.fretscale.model.PitchClass;

/**
 * Degree-based spelling: the letter is fixed by the scale degree, the
 * accidental is whatever makes the letter land on the target pitch class.
 * This is why A blues spells its b5 as E-flat and not D-sharp.
 */
public final class NoteSpeller {

    private NoteSpeller() {
    }

    public static NoteName spell(int tonicLetterIndex, int letterOffset, int targetPitchClass) {
        int letterIndex = Math.floorMod(tonicLetterIndex + letterOffset, 7);
        int natural = PitchClass.naturalPitchClass(letterIndex);
        int diff = PitchClass.normalize(targetPitchClass - natural);
        int accidental = switch (diff) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 10 -> -2;
            case 11 -> -1;
            default -> 0;
        };
        return new NoteName(letterIndex, accidental);
    }
}
