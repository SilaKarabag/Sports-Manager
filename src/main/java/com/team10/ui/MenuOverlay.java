package com.team10.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;
import java.io.File;

public class MenuOverlay {

    public static Button createMenuButton(MainWindow window) {
        Button b = new Button("☰");
        UIHelper.style(b);
        b.setPrefWidth(46);
        b.setPrefHeight(38);
        b.setOnAction(e -> showMenu(window));
        return b;
    }

    static void showMenu(MainWindow window) {
        Stage menu = new Stage();
        menu.initOwner(window.getStage());
        menu.initModality(Modality.APPLICATION_MODAL);
        menu.initStyle(StageStyle.TRANSPARENT);

        Button resume = new Button("▶  Resume");
        Button save   = new Button("💾  Save Game");
        Button load   = new Button("📂  Load Game");
        Button main   = new Button("🏠  Main Menu");
        Button sound  = new Button(AudioManager.getSoundText());
        Button quit   = new Button("✖  Quit");

        for (Button b : new Button[]{resume, save, load, main, sound, quit}) {
            UIHelper.style(b);
        }

        save.setDisable(!window.getController().hasActiveSession());

        resume.setOnAction(e -> menu.close());

        save.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save Game");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Save File (*.dat)", "*.dat"));
            fc.setInitialFileName("savegame");
            File f = fc.showSaveDialog(window.getStage());
            if (f != null) {
                try {
                    window.getController().saveGame(f.getAbsolutePath());
                    alert(Alert.AlertType.INFORMATION, "Saved", "Game saved successfully.");
                    menu.close();
                } catch (Exception ex) {
                    alert(Alert.AlertType.ERROR, "Save Failed", ex.getMessage());
                }
            }
        });

        load.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Load Game");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Save File (*.dat)", "*.dat"));
            File f = fc.showOpenDialog(window.getStage());
            if (f != null) {
                try {
                    window.getController().loadGame(f.getAbsolutePath());
                    menu.close();
                    window.showLeague();
                } catch (Exception ex) {
                    alert(Alert.AlertType.ERROR, "Load Failed", ex.getMessage());
                }
            }
        });

        main.setOnAction(e -> {
            AudioManager.stopBGM();
            menu.close();
            window.showMainMenu();
        });

        sound.setOnAction(e -> {
            AudioManager.toggleSound();
            sound.setText(AudioManager.getSoundText());
        });

        quit.setOnAction(e -> Platform.exit());

        VBox box = new VBox(12, resume, save, load, main, sound, quit);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(28));
        box.setStyle(
                "-fx-background-color: rgba(10,20,30,0.97);" +
                        "-fx-border-color: rgba(79,195,247,0.5);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;");

        Scene sc = new Scene(box, 270, 420);
        sc.setFill(Color.TRANSPARENT);
        menu.setScene(sc);
        menu.showAndWait();
    }

    static void alert(Alert.AlertType t, String title, String msg) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}