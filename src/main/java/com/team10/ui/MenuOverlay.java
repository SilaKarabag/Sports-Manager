package com.team10.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

/**
 * BUG FIX: Orijinalde saveButton handler'ı hasActiveSession() kontrolü yapmıyordu.
 * MainMenu'den açılırsa (session yok) saveGame() → IllegalStateException.
 * FIX: hasActiveSession() kontrolü eklendi.
 */
public class MenuOverlay {

    public static Button createMenuButton(MainWindow window) {
        Button btn = new Button("☰");
        UIHelper.style(btn);
        btn.setPrefWidth(45);
        btn.setPrefHeight(36);
        btn.setOnAction(e -> showMenu(window));
        return btn;
    }

    private static void showMenu(MainWindow window) {
        Stage menu = new Stage();
        menu.initOwner(window.getStage());
        menu.initModality(Modality.APPLICATION_MODAL);
        menu.initStyle(StageStyle.TRANSPARENT);

        Button returnBtn = new Button("↩  Return");
        Button saveBtn   = new Button("💾  Save Game");
        Button mainMenu  = new Button("🏠  Main Menu");
        Button soundBtn  = new Button(AudioManager.getSoundText());
        Button quit      = new Button("✖  Quit");

        for (Button b : new Button[]{returnBtn, saveBtn, mainMenu, soundBtn, quit}) UIHelper.style(b);

        // BUG FIX: session yoksa save butonu devre dışı
        saveBtn.setDisable(!window.getController().hasActiveSession());

        returnBtn.setOnAction(e -> menu.close());

        saveBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save Game");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Save File", "*.dat"));
            fc.setInitialFileName("savegame");
            File file = fc.showSaveDialog(window.getStage());
            if (file != null) {
                try {
                    window.getController().saveGame(file.getAbsolutePath());
                    showAlert(Alert.AlertType.INFORMATION, "Saved", "Game saved successfully.");
                    menu.close();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Save Failed", ex.getMessage());
                }
            }
        });

        mainMenu.setOnAction(e -> { AudioManager.stopBGM(); menu.close(); window.showMainMenu(); });

        soundBtn.setOnAction(e -> {
            AudioManager.toggleSound();
            soundBtn.setText(AudioManager.getSoundText());
        });

        quit.setOnAction(e -> Platform.exit());

        VBox root = new VBox(12, returnBtn, saveBtn, mainMenu, soundBtn, quit);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(28));
        root.setStyle(
                "-fx-background-color: rgba(10,20,30,0.97);" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-width: 1.5; -fx-background-radius: 12; -fx-border-radius: 12;"
        );

        Scene scene = new Scene(root, 260, 340);
        scene.setFill(Color.TRANSPARENT);
        menu.setScene(scene);
        menu.showAndWait();
    }

    private static void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title); a.setHeaderText(title); a.setContentText(msg);
        a.showAndWait();
    }
}
