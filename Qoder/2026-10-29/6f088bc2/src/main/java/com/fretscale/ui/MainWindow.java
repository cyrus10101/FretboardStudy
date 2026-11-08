package com.fretscale.ui;

import com.fretscale.controller.FretboardController;
import com.fretscale.model.DisplayMode;
import com.fretscale.model.ScaleType;
import com.fretscale.ui.components.AppHeader;
import com.fretscale.ui.components.ControlPanel;
import com.fretscale.ui.components.FretboardView;
import com.fretscale.ui.components.InfoPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class MainWindow extends VBox {

    private final FretboardController controller;

    public MainWindow() {
        getStyleClass().add("app-root");

        FretboardView fretboard = new FretboardView();
        InfoPanel infoPanel = new InfoPanel();
        controller = new FretboardController(fretboard, infoPanel);

        Region accent = new Region();
        accent.getStyleClass().add("top-accent");
        accent.setMinHeight(3);
        accent.setMaxHeight(3);

        AppHeader header = new AppHeader();
        ControlPanel controls = new ControlPanel(controller);

        VBox boardCard = buildBoardCard(fretboard);
        VBox.setVgrow(boardCard, Priority.ALWAYS);

        VBox boardWrap = new VBox(boardCard);
        boardWrap.getStyleClass().add("board-wrap");
        VBox.setVgrow(boardWrap, Priority.ALWAYS);

        getChildren().addAll(accent, header, controls, boardWrap, infoPanel);

        controller.show("C", ScaleType.MAJOR, DisplayMode.NOTES, false);
    }

    public FretboardController controller() {
        return controller;
    }

    private static VBox buildBoardCard(FretboardView board) {
        Label title = new Label("Fretboard");
        title.getStyleClass().add("card-title");
        Label subtitle = new Label("Standard tuning · E A D G B E · 12 frets");
        subtitle.getStyleClass().add("card-subtitle");
        VBox texts = new VBox(2, title, subtitle);

        HBox legend = new HBox(16,
                legendItem("#A78BFA", "Root"),
                legendItem("#38BDF8", "Scale tone"),
                legendItem(null, "Chromatic"));
        legend.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(12, texts, spacer, legend);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(10, headerRow, board);
        card.getStyleClass().add("board-card");
        card.setPadding(new Insets(14, 18, 14, 18));
        VBox.setVgrow(board, Priority.ALWAYS);
        return card;
    }

    private static HBox legendItem(String color, String text) {
        Circle dot = new Circle(4.5);
        if (color == null) {
            dot.setFill(Color.TRANSPARENT);
            dot.setStroke(Color.web("#64748B"));
            dot.setStrokeWidth(1.2);
        } else {
            dot.setFill(Color.web(color));
        }
        Label label = new Label(text);
        label.getStyleClass().add("legend-label");
        HBox item = new HBox(6, dot, label);
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }
}
