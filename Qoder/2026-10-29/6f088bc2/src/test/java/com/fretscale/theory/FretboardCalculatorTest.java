package com.fretscale.theory;

import com.fretscale.model.FretboardNote;
import com.fretscale.model.GuitarString;
import com.fretscale.model.Key;
import com.fretscale.model.Scale;
import com.fretscale.model.ScaleType;
import com.fretscale.model.Tuning;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FretboardCalculatorTest {

    private static List<FretboardNote> positions(String key, ScaleType type) {
        Scale scale = ScaleEngine.create(Key.of(key), type);
        return FretboardCalculator.positions(scale, Tuning.STANDARD, Tuning.FRET_COUNT);
    }

    private static List<FretboardNote> stringPositions(List<FretboardNote> positions, int stringIndex) {
        return positions.stream().filter(p -> p.string().index() == stringIndex).toList();
    }

    @Test
    @DisplayName("Low E string chromatic walk matches the guitar")
    void lowEChromaticWalk() {
        List<FretboardNote> lowE = stringPositions(positions("C", ScaleType.MAJOR), 5);
        List<String> names = lowE.stream().map(FretboardNote::noteLabel).toList();
        assertEquals(List.of("E", "F", "F#", "G", "G#", "A", "A#", "B",
                "C", "C#", "D", "D#", "E"), names);
    }

    @Test
    @DisplayName("Six strings x 13 positions are generated")
    void boardSize() {
        List<FretboardNote> all = positions("C", ScaleType.MAJOR);
        assertEquals(Tuning.STANDARD.size() * (Tuning.FRET_COUNT + 1), all.size());
        for (GuitarString string : Tuning.STANDARD) {
            assertEquals(13, stringPositions(all, string.index()).size());
        }
    }

    @Test
    @DisplayName("Fret 12 repeats the open string on every string")
    void octaveAtFretTwelve() {
        List<FretboardNote> all = positions("C", ScaleType.MAJOR);
        for (GuitarString string : Tuning.STANDARD) {
            List<FretboardNote> positions = stringPositions(all, string.index());
            FretboardNote open = positions.get(0);
            FretboardNote twelfth = positions.get(12);
            assertEquals(open.pitchClass(), twelfth.pitchClass(), string.description());
            assertEquals(open.noteLabel(), twelfth.noteLabel(), string.description());
        }
    }

    @Test
    @DisplayName("Standard tuning open strings are E B G D A E")
    void standardTuning() {
        List<FretboardNote> all = positions("C", ScaleType.MAJOR);
        List<String> openNotes = all.stream()
                .filter(p -> p.fret() == 0)
                .map(FretboardNote::noteLabel)
                .toList();
        assertEquals(List.of("E", "B", "G", "D", "A", "E"), openNotes);
    }

    @Test
    @DisplayName("A single octave of one string covers all 12 pitch classes")
    void allTwelvePitchClassesOnOneString() {
        List<FretboardNote> lowE = stringPositions(positions("C", ScaleType.MAJOR), 5);
        Set<Integer> frettedOctave = lowE.stream()
                .filter(p -> p.fret() >= 1 && p.fret() <= 12)
                .map(FretboardNote::pitchClass)
                .collect(Collectors.toSet());
        assertEquals(12, frettedOctave.size());
    }

    @Test
    @DisplayName("C major highlights exactly the scale tones, tonic flagged on every C")
    void cMajorHighlighting() {
        List<FretboardNote> all = positions("C", ScaleType.MAJOR);
        for (FretboardNote note : all) {
            boolean expected = Set.of(0, 2, 4, 5, 7, 9, 11).contains(note.pitchClass());
            assertEquals(expected, note.inScale(), note.positionText());
            assertEquals(note.pitchClass() == 0, note.isTonic(), note.positionText());
        }
        long tonics = all.stream().filter(FretboardNote::isTonic).count();
        assertEquals(6, tonics, "every string has one C within 12 frets except where the nut lands on C");
    }

    @Test
    @DisplayName("A blues shows Eb on the D string at fret 1")
    void flatSpellingOnBoard() {
        List<FretboardNote> all = positions("A", ScaleType.BLUES);
        FretboardNote note = all.stream()
                .filter(p -> p.string().index() == 3 && p.fret() == 1)
                .findFirst()
                .orElseThrow();
        assertEquals("Eb", note.noteLabel());
        assertEquals("b5", note.intervalLabel());
    }

    @Test
    @DisplayName("Interval labels follow the scale, not the pitch class")
    void intervalLabels() {
        List<FretboardNote> all = positions("C", ScaleType.MAJOR);
        FretboardNote highEfret0 = all.stream()
                .filter(p -> p.string().index() == 0 && p.fret() == 0)
                .findFirst().orElseThrow();
        assertEquals("3", highEfret0.intervalLabel());

        FretboardNote lowEfret8 = all.stream()
                .filter(p -> p.string().index() == 5 && p.fret() == 8)
                .findFirst().orElseThrow();
        assertEquals("1", lowEfret8.intervalLabel());
    }

    @Test
    @DisplayName("Notes outside the scale carry no interval label")
    void nonScaleNotesHaveNoInterval() {
        List<FretboardNote> all = positions("C", ScaleType.MAJOR);
        all.stream().filter(p -> !p.inScale()).forEach(p -> assertEquals("", p.intervalLabel()));
    }

    @Test
    @DisplayName("The triad filter keeps only root, third and fifth tones")
    void triadMembership() {
        List<FretboardNote> all = positions("C", ScaleType.MAJOR);
        Set<Integer> triad = Set.of(0, 4, 7);
        all.forEach(note -> {
            if (note.inTriad()) {
                assertTrue(triad.contains(note.pitchClass()), note.positionText());
            }
        });
        long triadCount = all.stream().filter(FretboardNote::inTriad).count();
        assertTrue(triadCount > 0);
        assertFalse(all.stream().anyMatch(p -> p.pitchClass() == 2 && p.inTriad()));
    }
}
