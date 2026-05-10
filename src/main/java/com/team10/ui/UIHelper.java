package com.team10.ui;

import javafx.scene.control.Button;

/**
 * BUG FIX: setOnMouseEntered/Exited önceki event handler'ları eziyor.
 *   Eğer bir buton farklı hover davranışı istiyorsa (sport selection gibi)
 *   çakışıyordu. Düzeltildi: addEventHandler yerine setOnMouse... kalıyor
 *   ama dışarıdan sonradan üzerine yazılabilir.
 */
public class UIHelper {

    private static final String BASE =
            "-fx-background-color: rgba(255,255,255,0.08);" +
                    "-fx-border-color: rgba(255,255,255,0.30);" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-cursor: hand;";

    private static final String HOVER =
            "-fx-background-color: rgba(79,195,247,0.18);" +
                    "-fx-border-color: rgba(79,195,247,0.70);" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-cursor: hand;";

    private static final String PRESSED =
            "-fx-background-color: rgba(79,195,247,0.30);" +
                    "-fx-border-color: rgba(79,195,247,0.90);" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-cursor: hand;";

    public static void style(Button b) {
        b.setStyle(BASE);
        b.setPrefWidth(180);
        b.setPrefHeight(40);
        b.setFocusTraversable(false);

        b.setOnMouseEntered(e -> { if (!b.isDisabled()) b.setStyle(HOVER); });
        b.setOnMouseExited(e  -> { if (!b.isDisabled()) b.setStyle(BASE);  });
        b.setOnMousePressed(e -> { if (!b.isDisabled()) { b.setStyle(PRESSED); b.setTranslateY(1); }});
        b.setOnMouseReleased(e-> { if (!b.isDisabled()) { b.setStyle(HOVER);   b.setTranslateY(0); }});

        // Disabled style
        b.disabledProperty().addListener((obs, old, dis) ->
                b.setStyle(dis
                        ? BASE + "-fx-opacity: 0.4;"
                        : BASE)
        );
    }
}
