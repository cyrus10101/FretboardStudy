package com.fretscale.model;

import java.util.List;
import java.util.Set;

public record Triad(List<ScaleDegree> tones, String label) {

    public Set<Integer> pitchClasses() {
        return tones.stream().mapToInt(ScaleDegree::pitchClass).boxed().collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    public boolean contains(int pitchClass) {
        return tones.stream().anyMatch(t -> t.pitchClass() == pitchClass);
    }
}
