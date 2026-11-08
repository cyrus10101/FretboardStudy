package com.fretscale.app;

import com.fretscale.ui.MainWindow;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FretscaleApp extends Application {

    @Override
    public void start(Stage stage) {
        MainWindow root = new MainWindow();

        Scene scene = new Scene(root, 1360, 880);
        scene.setFill(Color.web("#0B1020"));
        scene.getStylesheets().add(getClass().getResource("/styles/fretscale.css").toExternalForm());

        stage.setTitle("FRETSCALE — Guitar Theory Made Easy");
        stage.setMinWidth(1120);
        stage.setMinHeight(780);
        stage.setScene(scene);
        stage.show();

        root.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(420), root);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
