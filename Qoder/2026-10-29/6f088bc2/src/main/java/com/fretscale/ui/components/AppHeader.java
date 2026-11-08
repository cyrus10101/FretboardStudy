package com.fretscale.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public final class AppHeader extends HBox {

    public AppHeader() {
        getStyleClass().add("app-header");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(14);
        setPadding(new Insets(16, 28, 16, 26));
        setMinHeight(Region.USE_PREF_SIZE);

        StackPane badge = new StackPane(new GuitarIcon(34));
        badge.getStyleClass().add("logo-badge");
        badge.setPadding(new Insets(9));

        Text fret = new Text("FRET");
        fret.setFill(Color.web("#F8FAFC"));
        fret.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 30));
        Text scale = new Text("SCALE");
        scale.setFill(Color.web("#A78BFA"));
        scale.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 30));
        TextFlow wordmark = new TextFlow(fret, scale);

        Label tagline = new Label("GUITAR THEORY MADE EASY");
        tagline.getStyleClass().add("tagline");

        VBox brand = new VBox(0, wordmark, tagline);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label learn = meta("Learn");
        Label explore = meta("Explore");
        Label play = meta("Play");
        HBox metaRow = new HBox(10, learn, dot(), explore, dot(), play);
        metaRow.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(badge, brand, spacer, metaRow);
    }

    private static Label meta(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("header-meta");
        return label;
    }

    private static Circle dot() {
        return new Circle(2.5, Color.web("#7C3AED"));
    }
}
