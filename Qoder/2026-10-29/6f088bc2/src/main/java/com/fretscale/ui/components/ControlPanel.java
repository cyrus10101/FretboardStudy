package com.fretscale.ui.components;

import com.fretscale.controller.FretboardController;
import com.fretscale.model.DisplayMode;
import com.fretscale.model.Key;
import com.fretscale.model.ScaleType;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public final class ControlPanel extends VBox {

    private final FretboardController controller;

    private final ComboBox<String> keyBox = new ComboBox<>();
    private final ComboBox<DisplayMode> displayBox = new ComboBox<>();
    private final ComboBox<ScaleType> scaleBox = new ComboBox<>();
    private final CheckBox triadCheck = new CheckBox("Only Triad Notes");
    private final CheckBox chromaticCheck = new CheckBox("Show chromatic notes");
    private final CheckBox fretNumbersCheck = new CheckBox("Show fret numbers");
    private final ToggleButton menuToggle = new ToggleButton("☰   Menu");
    private final VBox drawer = new VBox(12);

    public ControlPanel(FretboardController controller) {
        this.controller = controller;
        getStyleClass().add("control-panel");
        setSpacing(12);
        setPadding(new Insets(16, 24, 6, 24));
        setMinHeight(Region.USE_PREF_SIZE);
        setMaxHeight(Region.USE_PREF_SIZE);

        HBox row = buildControlRow();
        buildDrawer();
        getChildren().addAll(row, drawer);
    }

    private HBox buildControlRow() {
        keyBox.getItems().addAll(Key.chromaticNames());
        keyBox.setValue("C");
        keyBox.setPrefWidth(96);
        keyBox.setId("key-selector");

        displayBox.getItems().addAll(DisplayMode.values());
        displayBox.setValue(DisplayMode.NOTES);
        displayBox.setPrefWidth(136);
        displayBox.setId("display-selector");

        scaleBox.getItems().addAll(ScaleType.values());
        scaleBox.setValue(ScaleType.MAJOR);
        scaleBox.setPrefWidth(224);
        scaleBox.setId("scale-selector");

        menuToggle.getStyleClass().add("btn-ghost");
        menuToggle.setSelected(false);
        menuToggle.setOnAction(e -> setDrawerOpen(menuToggle.isSelected()));

        triadCheck.setId("triad-toggle");

        Button showButton = new Button("▶   Show");
        showButton.getStyleClass().add("btn-primary");
        showButton.setOnAction(e -> applySelection());
        showButton.setOnMouseEntered(e -> animate(showButton, 1.03));
        showButton.setOnMouseExited(e -> animate(showButton, 1.0));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(18, menuToggle, field("KEY", keyBox), field("DISPLAY", displayBox),
                field("SCALE", scaleBox), triadCheck, spacer, showButton);
        row.getStyleClass().add("control-card");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void buildDrawer() {
        drawer.getStyleClass().add("drawer");
        drawer.setSpacing(10);
        drawer.setVisible(false);
        drawer.setManaged(false);

        Label heading = new Label("DISPLAY SETTINGS");
        heading.getStyleClass().add("eyebrow");

        chromaticCheck.setSelected(true);
        chromaticCheck.setId("chromatic-toggle");
        chromaticCheck.selectedProperty().addListener((obs, was, is) -> pushDisplayOptions());

        fretNumbersCheck.setSelected(true);
        fretNumbersCheck.setId("fretnumbers-toggle");
        fretNumbersCheck.selectedProperty().addListener((obs, was, is) -> pushDisplayOptions());

        Button reset = new Button("Reset");
        reset.getStyleClass().add("btn-ghost");
        reset.setOnAction(e -> resetSelection());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toggles = new HBox(22, heading, spacer, chromaticCheck, fretNumbersCheck, reset);
        toggles.setAlignment(Pos.CENTER_LEFT);
        toggles.setPadding(new Insets(0, 0, 0, 2));

        drawer.getChildren().add(toggles);
    }

    private void applySelection() {
        controller.show(keyBox.getValue(), scaleBox.getValue(),
                displayBox.getValue(), triadCheck.isSelected());
    }

    private void pushDisplayOptions() {
        controller.setDisplayOptions(chromaticCheck.isSelected(), fretNumbersCheck.isSelected());
    }

    private void resetSelection() {
        keyBox.setValue("C");
        displayBox.setValue(DisplayMode.NOTES);
        scaleBox.setValue(ScaleType.MAJOR);
        triadCheck.setSelected(false);
        chromaticCheck.setSelected(true);
        fretNumbersCheck.setSelected(true);
        applySelection();
    }

    private void setDrawerOpen(boolean open) {
        if (open) {
            drawer.setManaged(true);
            drawer.setVisible(true);
            drawer.setOpacity(0);
            drawer.setTranslateY(-8);

            FadeTransition fade = new FadeTransition(Duration.millis(180), drawer);
            fade.setToValue(1);
            TranslateTransition slide = new TranslateTransition(Duration.millis(180), drawer);
            slide.setToY(0);
            new ParallelTransition(fade, slide).play();
        } else {
            FadeTransition fade = new FadeTransition(Duration.millis(140), drawer);
            fade.setToValue(0);
            fade.setOnFinished(e -> {
                drawer.setVisible(false);
                drawer.setManaged(false);
            });
            fade.play();
        }
    }

    private static VBox field(String label, Node control) {
        Label caption = new Label(label);
        caption.getStyleClass().add("field-label");
        VBox box = new VBox(6, caption, control);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private static void animate(Node node, double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(110), node);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }
}
