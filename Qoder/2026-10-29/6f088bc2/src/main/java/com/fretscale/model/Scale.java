package com.fretscale.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A fully calculated scale: spelled degrees plus the tonic triad drawn from
 * the scale's own 1st, 3rd and 5th degrees.
 */
public final class Scale {

    private final Key key;
    private final ScaleType type;
    private final List<ScaleDegree> degrees;
    private final Map<Integer, ScaleDegree> byPitchClass;
    private final Triad triad;

    public Scale(Key key, ScaleType type, List<ScaleDegree> degrees, Triad triad) {
        this.key = key;
        this.type = type;
        this.degrees = List.copyOf(degrees);
        this.triad = triad;
        Map<Integer, ScaleDegree> map = new LinkedHashMap<>();
        for (ScaleDegree degree : this.degrees) {
            map.putIfAbsent(degree.pitchClass(), degree);
        }
        this.byPitchClass = Map.copyOf(map);
    }

    public Key key() {
        return key;
    }

    public ScaleType type() {
        return type;
    }

    public List<ScaleDegree> degrees() {
        return degrees;
    }

    public Triad triad() {
        return triad;
    }

    public int tonicPitchClass() {
        return key.tonicPitchClass();
    }

    public boolean contains(int pitchClass) {
        return byPitchClass.containsKey(PitchClass.normalize(pitchClass));
    }

    public ScaleDegree degreeFor(int pitchClass) {
        return byPitchClass.get(PitchClass.normalize(pitchClass));
    }

    public Set<Integer> pitchClasses() {
        return byPitchClass.keySet();
    }

    public String notesText() {
        return degrees.stream().map(d -> d.name().display()).reduce((a, b) -> a + " – " + b).orElse("");
    }

    public String formulaText() {
        return degrees.stream().map(ScaleDegree::intervalLabel).reduce((a, b) -> a + " – " + b).orElse("");
    }

    public String scaleLabel() {
        return key.name() + " " + type.displayName();
    }
}
