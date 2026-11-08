package com.fretscale.ui.components;

import com.fretscale.model.DisplayMode;
import com.fretscale.model.FretboardNote;
import com.fretscale.model.Scale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public final class InfoPanel extends HBox {

    private final Label keyValue = value();
    private final Label scaleValue = value();
    private final Label modeValue = value();
    private final Label filterValue = value();
    private final Label triadValue = value();

    private final Label notesValue = new Label();
    private final Label formulaValue = new Label();
    private final Label explanation = new Label();

    private final Label selectedNote = new Label("—");
    private final Label selectedPosition = new Label("Click any fret marker to inspect a note.");
    private final Label selectedStatus = new Label();
    private final Label selectedInterval = new Label();

    public InfoPanel() {
        getStyleClass().add("info-panel");
        setSpacing(14);
        setPadding(new Insets(2, 24, 16, 24));
        setMinHeight(Region.USE_PREF_SIZE);
        setMaxHeight(Region.USE_PREF_SIZE);

        VBox selectionCard = card("CURRENT SELECTION");
        selectionCard.setPrefWidth(300);
        selectionCard.setMinWidth(286);
        selectionCard.setMaxWidth(320);
        selectionCard.getChildren().add(selectionGrid());

        VBox notesCard = card("SCALE NOTES");
        notesValue.getStyleClass().add("info-notes");
        notesValue.setWrapText(true);
        formulaValue.getStyleClass().add("info-formula");
        explanation.getStyleClass().add("info-explanation");
        explanation.setWrapText(true);
        VBox notesBox = new VBox(6, notesValue, formulaValue, explanation);
        VBox.setVgrow(notesBox, Priority.ALWAYS);
        notesCard.getChildren().add(notesBox);
        HBox.setHgrow(notesCard, Priority.ALWAYS);

        VBox selectedCard = card("SELECTED NOTE");
        selectedCard.setPrefWidth(280);
        selectedCard.setMinWidth(264);
        selectedCard.setMaxWidth(300);
        selectedNote.getStyleClass().add("selected-note");
        selectedPosition.getStyleClass().add("info-explanation");
        selectedPosition.setWrapText(true);
        selectedStatus.getStyleClass().add("selected-status");
        selectedInterval.getStyleClass().add("selected-interval");
        VBox selectedBox = new VBox(3, selectedNote, selectedPosition, selectedStatus, selectedInterval);
        selectedCard.getChildren().add(selectedBox);

        getChildren().addAll(selectionCard, notesCard, selectedCard);
    }

    public void update(Scale scale, DisplayMode mode, boolean triadOnly) {
        keyValue.setText(scale.key().name());
        scaleValue.setText(scale.type().displayName());
        modeValue.setText(mode.displayName());
        filterValue.setText(triadOnly ? "Triad only" : "All scale tones");
        triadValue.setText(scale.triad().label());

        notesValue.setText(scale.notesText());
        formulaValue.setText(scale.formulaText());
        explanation.setText(scale.type().explanation());

        selectedNote.setText("—");
        selectedPosition.setText("Click any fret marker to inspect a note.");
        selectedStatus.setText("");
        selectedInterval.setText("");
    }

    public void showSelection(FretboardNote note, Scale scale) {
        selectedNote.setText(note.noteLabel());
        selectedPosition.setText(note.positionText());

        if (!note.inScale()) {
            selectedStatus.setText("Not in " + scale.scaleLabel());
            selectedInterval.setText("");
            return;
        }

        selectedInterval.setText("Interval " + note.intervalLabel());
        if (note.isTonic()) {
            selectedStatus.setText("Tonic · " + scale.scaleLabel());
        } else if (note.inTriad()) {
            selectedStatus.setText("Chord tone · " + scale.triad().label());
        } else {
            selectedStatus.setText("Scale tone · " + scale.scaleLabel());
        }
    }

    private GridPane selectionGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(5);
        addRow(grid, 0, "Key", keyValue);
        addRow(grid, 1, "Scale", scaleValue);
        addRow(grid, 2, "Display", modeValue);
        addRow(grid, 3, "Filter", filterValue);
        addRow(grid, 4, "Triad", triadValue);
        return grid;
    }

    private static void addRow(GridPane grid, int row, String caption, Label valueNode) {
        Label captionLabel = new Label(caption);
        captionLabel.getStyleClass().add("info-key");
        captionLabel.setMinWidth(58);
        grid.addRow(row, captionLabel, valueNode);
    }

    private static VBox card(String title) {
        Label heading = new Label(title);
        heading.getStyleClass().add("eyebrow");
        VBox box = new VBox(8, heading);
        box.getStyleClass().add("info-card");
        box.setPadding(new Insets(10, 14, 10, 14));
        box.setAlignment(Pos.TOP_LEFT);
        box.setMinHeight(104);
        return box;
    }

    private static Label value() {
        Label label = new Label();
        label.getStyleClass().add("info-value");
        return label;
    }
}
