package com.team10.ui;

import javafx.scene.control.Button;

public class UIHelper {

    private static final String NORMAL_STYLE =
        "-fx-background-color: rgba(255,255,255,0.06);" +
            "-fx-border-color: rgba(192,192,192,0.45);" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: silver;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: normal;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;";

    private static final String HOVER_STYLE =
        "-fx-background-color: rgba(255,255,255,0.14);" +
            "-fx-border-color: rgba(255,255,255,0.75);" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: normal;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;";

    public static void style(Button b) {

        b.setStyle(NORMAL_STYLE);

        b.setPrefWidth(170);
        b.setPrefHeight(36);

        b.setFocusTraversable(false);

        b.setOnMouseEntered(e -> b.setStyle(HOVER_STYLE));

        b.setOnMouseExited(e -> b.setStyle(NORMAL_STYLE));
    }

}