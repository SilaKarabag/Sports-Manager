package com.team10.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UIHelper {

    private static final String BASE =
            "-fx-background-color: rgba(255,255,255,0.08);" +
                    "-fx-border-color: rgba(255,255,255,0.28);" +
                    "-fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8;" +
                    "-fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;";

    private static final String HOVER =
            "-fx-background-color: rgba(79,195,247,0.22);" +
                    "-fx-border-color: rgba(79,195,247,0.75);" +
                    "-fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8;" +
                    "-fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;";

    private static final String PRESSED =
            "-fx-background-color: rgba(79,195,247,0.40);" +
                    "-fx-border-color: #4fc3f7;" +
                    "-fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8;" +
                    "-fx-text-fill: white; -fx-font-size: 14px;";

    private static final String DISABLED =
            "-fx-background-color: rgba(255,255,255,0.03);" +
                    "-fx-border-color: rgba(255,255,255,0.10);" +
                    "-fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8;" +
                    "-fx-text-fill: rgba(255,255,255,0.35); -fx-font-size: 14px;";

    public static void style(Button b) {
        b.setStyle(BASE);
        b.setPrefWidth(190);
        b.setPrefHeight(42);
        b.setFocusTraversable(false);

        b.setOnMouseEntered(e  -> { if (!b.isDisabled()) b.setStyle(HOVER); });
        b.setOnMouseExited(e   -> { if (!b.isDisabled()) b.setStyle(BASE); });
        b.setOnMousePressed(e  -> { if (!b.isDisabled()) { b.setStyle(PRESSED); b.setTranslateY(1); }});
        b.setOnMouseReleased(e -> { if (!b.isDisabled()) { b.setStyle(HOVER); b.setTranslateY(0); }});

        b.disabledProperty().addListener((obs, old, dis) ->
                b.setStyle(dis ? DISABLED : BASE));
    }

    public static void styleSmall(Button b) {
        style(b);
        b.setPrefWidth(130);
        b.setPrefHeight(34);
        b.setStyle(b.getStyle() + "-fx-font-size:12px;");
    }

    public static Label sectionTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #4fc3f7; -fx-font-size: 15px; -fx-font-weight: bold;");
        return l;
    }
}
