package com.fretscale.theory;

import com.fretscale.model.FretboardNote;
import com.fretscale.model.GuitarString;
import com.fretscale.model.PitchClass;
import com.fretscale.model.Scale;
import com.fretscale.model.ScaleDegree;

import java.util.ArrayList;
import java.util.List;

public final class FretboardCalculator {

    private FretboardCalculator() {
    }

    public static List<FretboardNote> positions(Scale scale, List<GuitarString> strings, int fretCount) {
        List<FretboardNote> positions = new ArrayList<>();
        for (GuitarString string : strings) {
            for (int fret = 0; fret <= fretCount; fret++) {
                int pitchClass = string.pitchClassAt(fret);
                ScaleDegree degree = scale.degreeFor(pitchClass);
                boolean inScale = degree != null;
                String noteLabel = inScale
                        ? degree.name().display()
                        : PitchClass.chromaticName(pitchClass, scale.key().prefersFlats());
                String intervalLabel = inScale ? degree.intervalLabel() : "";
                boolean tonic = inScale && pitchClass == scale.tonicPitchClass();
                boolean inTriad = inScale && scale.triad().contains(pitchClass);
                positions.add(new FretboardNote(string, fret, pitchClass,
                        noteLabel, intervalLabel, inScale, tonic, inTriad));
            }
        }
        return List.copyOf(positions);
    }
}
