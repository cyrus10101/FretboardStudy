package com.fretscale.theory;

import com.fretscale.model.Key;
import com.fretscale.model.Scale;
import com.fretscale.model.ScaleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriadBuilderTest {

    private static Scale scale(String key, ScaleType type) {
        return ScaleEngine.create(Key.of(key), type);
    }

    @Test
    @DisplayName("C major triad is C E G")
    void cMajorTriad() {
        Scale scale = scale("C", ScaleType.MAJOR);
        assertEquals(Set.of(0, 4, 7), scale.triad().pitchClasses());
        assertEquals("C major triad", scale.triad().label());
    }

    @Test
    @DisplayName("A natural minor triad is A C E")
    void aMinorTriad() {
        Scale scale = scale("A", ScaleType.NATURAL_MINOR);
        assertEquals(Set.of(9, 0, 4), scale.triad().pitchClasses());
        assertEquals("A minor triad", scale.triad().label());
    }

    @Test
    @DisplayName("Pentatonic scales still yield a complete triad")
    void pentatonicTriads() {
        assertEquals(Set.of(0, 4, 7), scale("C", ScaleType.MAJOR_PENTATONIC).triad().pitchClasses());
        assertEquals(Set.of(9, 0, 4), scale("A", ScaleType.MINOR_PENTATONIC).triad().pitchClasses());
    }

    @Test
    @DisplayName("Blues keeps the minor third and perfect fifth, ignoring the blue note")
    void bluesTriad() {
        Scale scale = scale("A", ScaleType.BLUES);
        assertEquals(Set.of(9, 0, 4), scale.triad().pitchClasses());
        assertEquals("A minor triad", scale.triad().label());
    }

    @Test
    @DisplayName("Harmonic major and melodic minor derive their own thirds")
    void alteredScales() {
        Scale harmonicMajor = scale("C", ScaleType.HARMONIC_MAJOR);
        assertEquals(Set.of(0, 4, 7), harmonicMajor.triad().pitchClasses());

        Scale melodicMinor = scale("C", ScaleType.MELODIC_MINOR);
        assertEquals(Set.of(0, 3, 7), melodicMinor.triad().pitchClasses());
        assertEquals("C minor triad", melodicMinor.triad().label());
    }

    @Test
    @DisplayName("Triad tones are always a subset of the scale")
    void triadIsSubsetOfScale() {
        for (ScaleType type : ScaleType.values()) {
            for (String key : Key.chromaticNames()) {
                Scale scale = scale(key, type);
                List<Integer> triad = scale.triad().tones().stream().map(t -> t.pitchClass()).toList();
                for (int pc : triad) {
                    assertEquals(true, scale.contains(pc), key + " " + type);
                }
            }
        }
    }
}
