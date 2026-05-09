package com.team10.ui;

import javafx.scene.control.Button;

public class UIHelper {

    private static final String NORMAL_STYLE =
        "-fx-background-color: rgba(0,0,0,0.45);" +
            "-fx-border-color: rgba(255,255,255,0.25);" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;";

    private static final String HOVER_STYLE =
        "-fx-background-color: rgba(0,0,0,0.60);" +
            "-fx-border-color: rgba(255,255,255,0.40);" +
            "-fx-text-fill: white;" +
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