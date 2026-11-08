# FRETSCALE

A native JavaFX desktop application for guitar fretboard visualisation and music theory study.

Six strings, 12 frets, standard tuning, seven scales, and note or interval labels — all calculated live from pitch classes, never hardcoded per fret.

## Run

Requires JDK 21 and Maven.

```bash
mvn clean javafx:run
```

## Test

```bash
mvn test
```

27 JUnit 5 tests cover scale spelling, interval labels, fretboard positions, octave repetition at fret 12, all 12 pitch classes, and triad filtering.

## Using the app

- **Key** — the 12 chromatic pitch classes; the tonic is spelled from the selected name.
- **Display** — `Notes` shows note names, `Intervals` shows degrees relative to the tonic (`1`, `b3`, `b5`, `7` …).
- **Scale** — Major, Natural Minor, Major Pentatonic, Minor Pentatonic, Blues, Harmonic Major, Melodic Minor (ascending).
- **Only Triad Notes** — restores the tonic triad: the scale's own 1st, 3rd and 5th degrees (C major → C E G, A minor → A C E). Every scale here contains those three letters, so the triad keeps the scale's quality instead of forcing a major triad. Blues keeps the perfect fifth and ignores the b5.
- **Show** — recalculates the board and the information panel.
- **Menu** — opens display settings: hide chromatic notes, hide fret numbers, reset.
- **Hover** a marker for its note, string, fret and interval; **click** to inspect it in the panel.

## Structure

```
com.fretscale
├── app          Application entry point
├── model        Pitch classes, keys, scales, tuning, fretboard notes
├── theory       Spelling, intervals, scale generation, triads, fretboard maths
├── controller   Selection state and view orchestration
├── ui           Main window
└── ui.components Header, control panel, fretboard view, info panel
```

The theory layer has no JavaFX dependencies and is fully testable on its own.

## Notes

- Spelling is degree-based: the letter comes from the scale degree, the accidental from the pitch class. A blues therefore spells its b5 as **Eb**, and C# major correctly uses E# and B#.
- Melodic minor is the ascending (jazz) form only.
- Rendering uses a canvas for the board (strings, wires, nut, inlays, fret numbers) and JavaFX nodes for note markers, so hover, tooltips and selection stay accessible.
