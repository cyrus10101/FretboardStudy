package com.fretscale.ui.components;

import com.fretscale.model.DisplayMode;
import com.fretscale.model.FretboardNote;
import com.fretscale.model.GuitarString;
import com.fretscale.model.Tuning;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class FretboardView extends Region {

    private static final double PAD_X = 18;
    private static final double PAD_TOP = 22;
    private static final double FRET_LABEL_BAND = 30;
    private static final int FRET_COUNT = Tuning.FRET_COUNT;

    private final Canvas canvas = new Canvas();
    private final Pane markerLayer = new Pane();
    private final List<Marker> markers = new ArrayList<>();

    private DisplayMode mode = DisplayMode.NOTES;
    private boolean triadOnly;
    private boolean showChromatic = true;
    private boolean showFretNumbers = true;
    private double markerRadius = 14;
    private Marker selected;
    private Consumer<FretboardNote> onNoteSelected = note -> {
    };

    public FretboardView() {
        getStyleClass().add("fretboard-view");
        markerLayer.setPickOnBounds(false);
        getChildren().addAll(canvas, markerLayer);
        setMinSize(640, 200);
        setPrefSize(1100, 460);
    }

    public void setOnNoteSelected(Consumer<FretboardNote> listener) {
        this.onNoteSelected = listener;
    }

    public void render(List<FretboardNote> notes, DisplayMode mode,
                       boolean triadOnly, boolean showChromatic, boolean showFretNumbers) {
        this.mode = mode;
        this.triadOnly = triadOnly;
        this.showChromatic = showChromatic;
        this.showFretNumbers = showFretNumbers;

        markerLayer.getChildren().clear();
        markers.clear();
        selected = null;
        for (FretboardNote note : notes) {
            Marker marker = new Marker(note);
            markers.add(marker);
            markerLayer.getChildren().add(marker);
        }
        applyMarkerStyles();
        requestLayout();
        Platform.runLater(this::playIntro);
    }

    public void setDisplayOptions(boolean showChromatic, boolean showFretNumbers) {
        this.showChromatic = showChromatic;
        this.showFretNumbers = showFretNumbers;
        applyMarkerStyles();
        if (getWidth() > 0 && getHeight() > 0) {
            drawBoard(geometry(getWidth(), getHeight()));
        }
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }

        canvas.setWidth(w);
        canvas.setHeight(h);
        canvas.relocate(0, 0);
        markerLayer.resizeRelocate(0, 0, w, h);

        Geometry geometry = geometry(w, h);
        drawBoard(geometry);
        layoutMarkers(geometry);
    }

    private Geometry geometry(double w, double h) {
        double openColumn = clamp(w * 0.062, 50, 82);
        double boardX = PAD_X + openColumn;
        double boardW = Math.max(160, w - PAD_X - boardX);
        double cellW = boardW / FRET_COUNT;
        double top = PAD_TOP;
        double bottom = h - FRET_LABEL_BAND;
        double rowH = Math.max(28, (bottom - top) / 6.0);
        double openX = PAD_X + openColumn * 0.5;
        return new Geometry(boardX, boardW, cellW, top, rowH, openX, PAD_X + 4, boardX + boardW - 4);
    }

    private void layoutMarkers(Geometry g) {
        double newRadius = clamp(Math.min(g.cellW(), g.rowH()) * 0.32, 10.5, 17.5);
        boolean radiusChanged = Math.abs(newRadius - markerRadius) > 0.2;
        markerRadius = newRadius;

        double size = (markerRadius + 3) * 2;
        for (Marker marker : markers) {
            marker.setPrefSize(size, size);
            marker.setLayoutX(g.xFor(marker.note.fret()) - size / 2);
            marker.setLayoutY(g.yFor(marker.note.string().index()) - size / 2);
        }
        if (radiusChanged) {
            applyMarkerStyles();
        }
    }

    private void drawBoard(Geometry g) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double plateX = PAD_X - 6;
        double plateY = g.top() - 12;
        double plateW = canvas.getWidth() - 2 * (PAD_X - 6);
        double plateH = g.boardHeight() + 24;

        gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#18213A")),
                new Stop(0.55, Color.web("#121A2E")),
                new Stop(1, Color.web("#0D1322"))));
        gc.fillRoundRect(plateX, plateY, plateW, plateH, 18, 18);
        gc.setStroke(Color.web("#243052"));
        gc.setLineWidth(1);
        gc.strokeRoundRect(plateX, plateY, plateW, plateH, 18, 18);

        gc.setFill(Color.web("#E2E8F0", 0.12));
        for (int fret : new int[]{3, 5, 7, 9}) {
            double x = g.xFor(fret);
            double y = g.top() + g.rowH() * 3;
            gc.fillOval(x - 5, y - 5, 10, 10);
        }
        double x12 = g.xFor(12);
        gc.fillOval(x12 - 5, g.top() + g.rowH() * 2 - 5, 10, 10);
        gc.fillOval(x12 - 5, g.top() + g.rowH() * 4 - 5, 10, 10);

        gc.setStroke(Color.web("#9DB0D6", 0.26));
        gc.setLineWidth(1);
        for (int fret = 1; fret <= FRET_COUNT; fret++) {
            double x = g.boardX() + g.cellW() * fret;
            gc.strokeLine(x, plateY + 4, x, plateY + plateH - 4);
        }

        gc.setStroke(Color.web("#9DB0D6", 0.42));
        double endWire = g.boardX() + g.boardW();
        gc.strokeLine(endWire, plateY + 4, endWire, plateY + plateH - 4);

        gc.setFill(new LinearGradient(0, 0, 1, 0, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#F8FAFC")),
                new Stop(1, Color.web("#94A3B8"))));
        gc.fillRoundRect(g.boardX() - 4, plateY + 2, 8, plateH - 4, 5, 5);

        for (GuitarString string : Tuning.STANDARD) {
            double y = g.yFor(string.index());
            double gauge = 1.1 + string.index() * 0.42;
            Color color = Color.web(string.color());

            gc.setStroke(color.deriveColor(0, 1, 1.05, 0.28));
            gc.setLineWidth(gauge + 2.5);
            gc.strokeLine(g.startX(), y, g.endX(), y);

            gc.setStroke(color.deriveColor(0, 1, 0.98, 0.95));
            gc.setLineWidth(gauge);
            gc.strokeLine(g.startX(), y, g.endX(), y);

            gc.setStroke(Color.web("#FFFFFF", 0.18));
            gc.setLineWidth(0.8);
            gc.strokeLine(g.startX(), y - gauge * 0.3, g.endX(), y - gauge * 0.3);
        }

        if (showFretNumbers) {
            gc.setFill(Color.web("#64748B"));
            gc.setFont(Font.font("Helvetica Neue", 11));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            double labelY = g.top() + g.boardHeight() + FRET_LABEL_BAND * 0.55;
            gc.fillText("0", g.openX(), labelY);
            for (int fret = 1; fret <= FRET_COUNT; fret++) {
                gc.fillText(String.valueOf(fret), g.boardX() + g.cellW() * (fret - 0.5), labelY);
            }
        }
    }

    private void applyMarkerStyles() {
        for (Marker marker : markers) {
            styleMarker(marker);
        }
    }

    private void styleMarker(Marker marker) {
        FretboardNote note = marker.note;
        boolean passesFilter = !triadOnly || note.inTriad();
        boolean active = note.inScale() && passesFilter;
        boolean visible = active || showChromatic;

        marker.setVisible(visible);
        marker.setMouseTransparent(!visible);

        double r = markerRadius;
        marker.disc.setRadius(r);
        marker.ring.setRadius(r + 2.6);
        marker.disc.setEffect(null);
        marker.ring.setStroke(Color.TRANSPARENT);
        marker.ring.setStrokeWidth(1.2);

        if (active) {
            if (note.isTonic()) {
                marker.disc.setFill(tonicFill());
                marker.disc.setStroke(Color.web("#FFFFFF", 0.75));
                marker.disc.setStrokeWidth(1.4);
                marker.label.setTextFill(Color.WHITE);
                DropShadow glow = new DropShadow(BlurType.GAUSSIAN, Color.web("#7C3AED", 0.7), 16, 0.35, 0, 2);
                marker.disc.setEffect(glow);
                marker.ring.setStroke(Color.web("#FFFFFF", 0.45));
            } else {
                Color base = Color.web(note.string().color());
                marker.disc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, base.interpolate(Color.WHITE, 0.26)),
                        new Stop(1, base.interpolate(Color.BLACK, 0.10))));
                marker.disc.setStroke(Color.web("#FFFFFF", 0.30));
                marker.disc.setStrokeWidth(1);
                marker.label.setTextFill(textColorOn(base));
                marker.disc.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web("#000000", 0.45), 8, 0.2, 0, 2));
            }
        } else {
            marker.disc.setFill(Color.web("#94A3B8", 0.06));
            marker.disc.setStroke(Color.web("#94A3B8", 0.22));
            marker.disc.setStrokeWidth(1);
            marker.label.setTextFill(Color.web("#64748B", 0.9));
        }

        if (marker == selected) {
            marker.ring.setStroke(Color.WHITE);
            marker.ring.setStrokeWidth(2.4);
        }

        String text = mode == DisplayMode.INTERVALS
                ? (note.inScale() ? note.intervalLabel() : "")
                : note.noteLabel();
        marker.label.setText(text);
        marker.label.setFont(Font.font("Helvetica Neue", FontWeight.BOLD,
                markerRadius >= 15 ? 12.5 : 11));
        marker.tooltip.setText(tooltipText(note));
    }

    private String tooltipText(FretboardNote note) {
        StringBuilder text = new StringBuilder(note.tooltipText());
        if (note.inScale()) {
            text.append("\nInterval: ").append(note.intervalLabel());
        }
        if (note.isTonic()) {
            text.append("\nTonic of the selected scale");
        }
        return text.toString();
    }

    private void select(Marker marker) {
        Marker previous = selected;
        selected = marker;
        if (previous != null) {
            styleMarker(previous);
        }
        styleMarker(marker);
        onNoteSelected.accept(marker.note);
    }

    private void playIntro() {
        List<Animation> animations = new ArrayList<>();
        for (Marker marker : markers) {
            if (!marker.isVisible()) {
                continue;
            }
            marker.setOpacity(0);
            FadeTransition fade = new FadeTransition(Duration.millis(170), marker);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(Math.min(marker.note.fret() * 16, 260)));
            animations.add(fade);
        }
        if (animations.isEmpty()) {
            return;
        }
        new ParallelTransition(animations.toArray(new Animation[0])).play();
    }

    private static LinearGradient tonicFill() {
        return new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#B79CFF")),
                new Stop(1, Color.web("#6D28D9")));
    }

    private static Color textColorOn(Color background) {
        double luminance = 0.299 * background.getRed()
                + 0.587 * background.getGreen()
                + 0.114 * background.getBlue();
        return luminance > 0.55 ? Color.web("#0B1020") : Color.WHITE;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private record Geometry(double boardX, double boardW, double cellW, double top,
                            double rowH, double openX, double startX, double endX) {

        double yFor(int stringIndex) {
            return top + rowH * (stringIndex + 0.5);
        }

        double xFor(int fret) {
            return fret == 0 ? openX : boardX + cellW * (fret - 0.5);
        }

        double boardHeight() {
            return rowH * 6;
        }
    }

    private final class Marker extends StackPane {

        private final FretboardNote note;
        private final Circle ring = new Circle();
        private final Circle disc = new Circle();
        private final Label label = new Label();
        private final Tooltip tooltip;

        private Marker(FretboardNote note) {
            this.note = note;
            getStyleClass().add("note-marker");
            setId("marker-" + note.string().index() + "-" + note.fret());
            disc.setMouseTransparent(true);
            ring.setMouseTransparent(true);
            label.setMouseTransparent(true);
            getChildren().addAll(ring, disc, label);

            tooltip = new Tooltip(tooltipText(note));
            tooltip.setShowDelay(Duration.millis(120));
            tooltip.setHideDelay(Duration.millis(80));
            Tooltip.install(this, tooltip);

            setCursor(Cursor.HAND);
            setOnMouseEntered(e -> scaleTo(1.15));
            setOnMouseExited(e -> scaleTo(this == selected ? 1.08 : 1.0));
            setOnMouseClicked(e -> {
                select(this);
                e.consume();
            });
        }

        private void scaleTo(double value) {
            ScaleTransition transition = new ScaleTransition(Duration.millis(110), this);
            transition.setToX(value);
            transition.setToY(value);
            transition.play();
        }
    }
}
