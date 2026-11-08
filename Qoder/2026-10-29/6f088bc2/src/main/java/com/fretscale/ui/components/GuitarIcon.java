package com.fretscale.ui.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Minimal guitar mark drawn with JavaFX shapes, so no icon font or image
 * asset is needed.
 */
public final class GuitarIcon extends Group {

    public GuitarIcon(double size) {
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#A78BFA")),
                new Stop(1, Color.web("#4F46E5")));

        Circle lowerBout = new Circle(size * 0.27, gradient);
        lowerBout.setCenterX(size * 0.5);
        lowerBout.setCenterY(size * 0.70);

        Circle upperBout = new Circle(size * 0.20, gradient);
        upperBout.setCenterX(size * 0.5);
        upperBout.setCenterY(size * 0.50);

        Circle soundHole = new Circle(size * 0.095);
        soundHole.setCenterX(size * 0.5);
        soundHole.setCenterY(size * 0.70);
        soundHole.setFill(Color.web("#10182C"));

        Rectangle neck = new Rectangle(size * 0.10, size * 0.38);
        neck.setX(size * 0.5 - size * 0.05);
        neck.setY(size * 0.08);
        neck.setArcWidth(size * 0.05);
        neck.setArcHeight(size * 0.05);
        neck.setFill(gradient);

        Rectangle headstock = new Rectangle(size * 0.18, size * 0.11);
        headstock.setX(size * 0.5 - size * 0.09);
        headstock.setY(0);
        headstock.setArcWidth(size * 0.07);
        headstock.setArcHeight(size * 0.07);
        headstock.setFill(gradient);

        Circle tunerLeft = new Circle(size * 0.020, Color.web("#C7B4FF"));
        tunerLeft.setCenterX(size * 0.37);
        tunerLeft.setCenterY(size * 0.045);

        Circle tunerRight = new Circle(size * 0.020, Color.web("#C7B4FF"));
        tunerRight.setCenterX(size * 0.63);
        tunerRight.setCenterY(size * 0.045);

        getChildren().addAll(neck, headstock, upperBout, lowerBout, soundHole, tunerLeft, tunerRight);
    }
}
