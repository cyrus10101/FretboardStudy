package com.fretscale.theory;

import com.fretscale.model.ScaleDegree;
import com.fretscale.model.ScaleType;
import com.fretscale.model.Triad;

import java.util.List;

/**
 * The tonic triad is taken from the scale's own 1st, 3rd and 5th degrees.
 * Every scale in this application contains those three letters, so the triad
 * follows the scale's colour (major, minor, diminished or augmented) instead
 * of being forced to a fixed major triad.
 */
public final class TriadBuilder {

    private TriadBuilder() {
    }

    public static Triad from(ScaleType type, List<ScaleDegree> degrees) {
        ScaleType.Degree[] definitions = type.degrees();
        ScaleDegree root = find(definitions, degrees, 0, 0);
        ScaleDegree third = find(definitions, degrees, 2, null);
        // Blues carries both b5 and 5 on the same letter; the perfect fifth wins.
        ScaleDegree fifth = find(definitions, degrees, 4, 7);

        if (root == null || third == null || fifth == null) {
            ScaleDegree tonic = root != null ? root : degrees.get(0);
            return new Triad(List.of(tonic), tonic.name().display() + " tonic");
        }

        String quality = switch (third.semitone()) {
            case 3 -> fifth.semitone() == 6 ? "diminished" : "minor";
            case 4 -> fifth.semitone() == 8 ? "augmented" : "major";
            default -> "tonic";
        };

        return new Triad(List.of(root, third, fifth),
                root.name().display() + " " + quality + " triad");
    }

    private static ScaleDegree find(ScaleType.Degree[] definitions, List<ScaleDegree> degrees,
                                    int letterOffset, Integer preferredSemitone) {
        ScaleDegree fallback = null;
        for (int i = 0; i < definitions.length; i++) {
            if (Math.floorMod(definitions[i].letterOffset(), 7) != letterOffset) {
                continue;
            }
            ScaleDegree degree = degrees.get(i);
            if (preferredSemitone == null || degree.semitone() == preferredSemitone) {
                return degree;
            }
            if (fallback == null) {
                fallback = degree;
            }
        }
        return fallback;
    }
}
