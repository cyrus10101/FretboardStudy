package com.fretscale.theory;

import com.fretscale.model.Key;
import com.fretscale.model.Scale;
import com.fretscale.model.ScaleDegree;
import com.fretscale.model.ScaleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScaleEngineTest {

    private static Scale scale(String key, ScaleType type) {
        return ScaleEngine.create(Key.of(key), type);
    }

    private static List<String> noteNames(Scale scale) {
        return scale.degrees().stream().map(d -> d.name().display()).toList();
    }

    private static List<String> intervals(Scale scale) {
        return scale.degrees().stream().map(ScaleDegree::intervalLabel).toList();
    }

    @Test
    @DisplayName("C Major spells C D E F G A B")
    void cMajor() {
        Scale scale = scale("C", ScaleType.MAJOR);
        assertEquals(List.of("C", "D", "E", "F", "G", "A", "B"), noteNames(scale));
        assertEquals(List.of("1", "2", "3", "4", "5", "6", "7"), intervals(scale));
        assertEquals("C – D – E – F – G – A – B", scale.notesText());
        assertEquals("1 – 2 – 3 – 4 – 5 – 6 – 7", scale.formulaText());
    }

    @Test
    @DisplayName("A Natural Minor spells A B C D E F G")
    void aNaturalMinor() {
        Scale scale = scale("A", ScaleType.NATURAL_MINOR);
        assertEquals(List.of("A", "B", "C", "D", "E", "F", "G"), noteNames(scale));
        assertEquals(List.of("1", "2", "b3", "4", "5", "b6", "b7"), intervals(scale));
    }

    @Test
    @DisplayName("C Major Pentatonic spells C D E G A")
    void cMajorPentatonic() {
        Scale scale = scale("C", ScaleType.MAJOR_PENTATONIC);
        assertEquals(List.of("C", "D", "E", "G", "A"), noteNames(scale));
        assertEquals(List.of("1", "2", "3", "5", "6"), intervals(scale));
    }

    @Test
    @DisplayName("A Minor Pentatonic spells A C D E G")
    void aMinorPentatonic() {
        Scale scale = scale("A", ScaleType.MINOR_PENTATONIC);
        assertEquals(List.of("A", "C", "D", "E", "G"), noteNames(scale));
        assertEquals(List.of("1", "b3", "4", "5", "b7"), intervals(scale));
    }

    @Test
    @DisplayName("A Minor Blues uses a flat fifth (Eb, not D#)")
    void aMinorBlues() {
        Scale scale = scale("A", ScaleType.BLUES);
        assertEquals(List.of("A", "C", "D", "Eb", "E", "G"), noteNames(scale));
        assertEquals(List.of("1", "b3", "4", "b5", "5", "b7"), intervals(scale));
    }

    @Test
    @DisplayName("C Harmonic Major spells C D E F G Ab B")
    void cHarmonicMajor() {
        Scale scale = scale("C", ScaleType.HARMONIC_MAJOR);
        assertEquals(List.of("C", "D", "E", "F", "G", "Ab", "B"), noteNames(scale));
        assertEquals(List.of("1", "2", "3", "4", "5", "b6", "7"), intervals(scale));
    }

    @Test
    @DisplayName("C Melodic Minor (ascending) spells C D Eb F G A B")
    void cMelodicMinor() {
        Scale scale = scale("C", ScaleType.MELODIC_MINOR);
        assertEquals(List.of("C", "D", "Eb", "F", "G", "A", "B"), noteNames(scale));
        assertEquals(List.of("1", "2", "b3", "4", "5", "6", "7"), intervals(scale));
    }

    @Test
    @DisplayName("Sharp keys respell correctly, including E# and B#")
    void cSharpMajor() {
        Scale scale = scale("C#", ScaleType.MAJOR);
        assertEquals(List.of("C#", "D#", "E#", "F#", "G#", "A#", "B#"), noteNames(scale));
    }

    @Test
    @DisplayName("F major uses a flat seventh letter (Bb)")
    void fMajor() {
        Scale scale = scale("F", ScaleType.MAJOR);
        assertEquals(List.of("F", "G", "A", "Bb", "C", "D", "E"), noteNames(scale));
    }

    @Test
    @DisplayName("Every key produces seven distinct pitch classes for a diatonic scale")
    void allTwelveKeys() {
        for (String keyName : Key.chromaticNames()) {
            Scale scale = scale(keyName, ScaleType.MAJOR);
            assertEquals(7, scale.pitchClasses().size(), keyName);
            assertEquals(Key.of(keyName).tonicPitchClass(), scale.tonicPitchClass(), keyName);
            int tonic = Key.of(keyName).tonicPitchClass();
            assertTrue(scale.contains(tonic));
            assertFalse(scale.contains(tonic + 1));
        }
    }

    @Test
    @DisplayName("Scale degrees are stored in interval order")
    void degreesAreOrdered() {
        for (ScaleType type : ScaleType.values()) {
            List<ScaleDegree> degrees = scale("D", type).degrees();
            for (int i = 1; i < degrees.size(); i++) {
                assertTrue(degrees.get(i).semitone() > degrees.get(i - 1).semitone(),
                        type + " degrees must ascend");
            }
        }
    }
}
