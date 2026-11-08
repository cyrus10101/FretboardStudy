package com.fretscale.theory;

import com.fretscale.model.PitchClass;

/**
 * Interval labels relative to the tonic (1, b3, b5, 7 …).
 */
public final class IntervalNamer {

    private static final String[] LABELS =
            {"1", "b2", "2", "b3", "3", "4", "b5", "5", "b6", "6", "b7", "7"};

    private IntervalNamer() {
    }

    public static String label(int semitonesFromTonic) {
        return LABELS[PitchClass.normalize(semitonesFromTonic)];
    }
}
