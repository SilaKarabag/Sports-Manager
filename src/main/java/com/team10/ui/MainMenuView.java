package com.team10.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * BUG FIX: Load Game sonrasında showLeague() çağrılıyor.
 * LeagueView → getSelectedSport() → controller.getSport() kullanıyor artık.
 * Bu sayede load sonrası NullPointerException oluşmuyor.
 */
public class MainMenuView {

    private final BorderPane root;

    public MainMenuView(MainWindow window) {
        root = new BorderPane();
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0d1117, #161b22, #1a1f2e);"
        );

        // Başlık
        Label title = new Label("SPORTS MANAGER");
        title.setStyle(
                "-fx-font-size: 52px; -fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-effect: dropshadow(gaussian, #4fc3f7, 30, 0.6, 0, 0);"
        );
        Label subtitle = new Label("League Simulation Game");
        subtitle.setStyle("-fx-text-fill: #4fc3f7; -fx-font-size: 16px;");

        VBox titleBlock = new VBox(6, title, subtitle);
        titleBlock.setAlignment(Pos.CENTER_LEFT);
        titleBlock.setPadding(new Insets(0, 0, 0, 60));

        // Butonlar
        Button start = new Button("🎮  Start New Game");
        Button load  = new Button("📂  Load Game");
        Button sound = new Button(AudioManager.getSoundText());
        Button quit  = new Button("✖  Quit");

        UIHelper.style(start); UIHelper.style(load);
        UIHelper.style(sound); UIHelper.style(quit);

        start.setOnAction(e -> window.showSportSelection());

        load.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Load Game");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Sports Manager Save File", "*.dat")
            );
            File file = fc.showOpenDialog(window.getStage());
            if (file != null) {
                try {
                    // BUG FIX: load sonrası showLeague() selectedSport'u session'dan alıyor
                    window.getController().loadGame(file.getAbsolutePath());
                    window.showLeague();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Load Failed",
                            "The save file could not be loaded.\n" + ex.getMessage());
                }
            }
        });

        sound.setOnAction(e -> {
            AudioManager.toggleSound();
            sound.setText(AudioManager.getSoundText());
        });

        quit.setOnAction(e -> Platform.exit());

        VBox menu = new VBox(14, start, load, sound, quit);
        menu.setAlignment(Pos.CENTER_RIGHT);
        menu.setPadding(new Insets(0, 70, 0, 0));

        // Dekoratif çizgi
        Region divider = new Region();
        divider.setPrefWidth(2);
        divider.setStyle("-fx-background-color: rgba(79,195,247,0.3);");
        divider.setPrefHeight(200);

        HBox center = new HBox(60, titleBlock, divider, menu);
        center.setAlignment(Pos.CENTER);

        root.setCenter(center);
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title); a.setHeaderText(title); a.setContentText(msg);
        a.showAndWait();
    }

    public Parent getRoot() { return root; }
}
