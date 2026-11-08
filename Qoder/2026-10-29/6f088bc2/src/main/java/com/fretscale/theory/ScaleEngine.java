package com.fretscale.theory;

import com.fretscale.model.Key;
import com.fretscale.model.NoteName;
import com.fretscale.model.PitchClass;
import com.fretscale.model.Scale;
import com.fretscale.model.ScaleDegree;
import com.fretscale.model.ScaleType;
import com.fretscale.model.Triad;

import java.util.ArrayList;
import java.util.List;

public final class ScaleEngine {

    private ScaleEngine() {
    }

    public static Scale create(Key key, ScaleType type) {
        List<ScaleDegree> degrees = new ArrayList<>();
        for (ScaleType.Degree degree : type.degrees()) {
            int pitchClass = PitchClass.normalize(key.tonicPitchClass() + degree.semitone());
            NoteName name = NoteSpeller.spell(key.tonic().letterIndex(), degree.letterOffset(), pitchClass);
            degrees.add(new ScaleDegree(pitchClass, name,
                    IntervalNamer.label(degree.semitone()), degree.semitone()));
        }
        Triad triad = TriadBuilder.from(type, degrees);
        return new Scale(key, type, degrees, triad);
    }
}
