package com.fretscale.controller;

import com.fretscale.model.DisplayMode;
import com.fretscale.model.FretboardNote;
import com.fretscale.model.Key;
import com.fretscale.model.Scale;
import com.fretscale.model.ScaleType;
import com.fretscale.model.Tuning;
import com.fretscale.theory.FretboardCalculator;
import com.fretscale.theory.ScaleEngine;
import com.fretscale.ui.components.FretboardView;
import com.fretscale.ui.components.InfoPanel;

import java.util.List;

/**
 * Holds the current selection, runs the theory calculation and pushes the
 * result into the view. Clicking a marker feeds the information panel.
 */
public final class FretboardController {

    private final FretboardView fretboardView;
    private final InfoPanel infoPanel;

    private Key key = Key.of("C");
    private ScaleType scaleType = ScaleType.MAJOR;
    private DisplayMode displayMode = DisplayMode.NOTES;
    private boolean triadOnly;
    private boolean showChromatic = true;
    private boolean showFretNumbers = true;
    private Scale currentScale = ScaleEngine.create(key, scaleType);

    public FretboardController(FretboardView fretboardView, InfoPanel infoPanel) {
        this.fretboardView = fretboardView;
        this.infoPanel = infoPanel;
        fretboardView.setOnNoteSelected(this::onNoteSelected);
    }

    public void show(String keyName, ScaleType type, DisplayMode mode, boolean triadOnly) {
        this.key = Key.of(keyName);
        this.scaleType = type;
        this.displayMode = mode;
        this.triadOnly = triadOnly;

        currentScale = ScaleEngine.create(key, type);
        List<FretboardNote> positions =
                FretboardCalculator.positions(currentScale, Tuning.STANDARD, Tuning.FRET_COUNT);

        fretboardView.render(positions, mode, triadOnly, showChromatic, showFretNumbers);
        infoPanel.update(currentScale, mode, triadOnly);
    }

    public void setDisplayOptions(boolean showChromatic, boolean showFretNumbers) {
        this.showChromatic = showChromatic;
        this.showFretNumbers = showFretNumbers;
        fretboardView.setDisplayOptions(showChromatic, showFretNumbers);
    }

    public DisplayMode currentMode() {
        return displayMode;
    }

    private void onNoteSelected(FretboardNote note) {
        infoPanel.showSelection(note, currentScale);
    }
}
