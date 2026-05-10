package com.team10.ui;

import java.io.File;
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

public class MenuOverlay {

    public static Button createMenuButton(MainWindow window) {
        Button menuButton = new Button("☰");
        UIHelper.style(menuButton);
        menuButton.setPrefWidth(45);
        menuButton.setPrefHeight(36);
        menuButton.setOnAction(e -> showMenu(window));
        return menuButton;
    }

    private static void showMenu(MainWindow window) {
        Stage menuStage = new Stage();
        menuStage.initOwner(window.getStage());
        menuStage.initModality(Modality.APPLICATION_MODAL);
        menuStage.initStyle(StageStyle.TRANSPARENT);

        Button returnButton = new Button("Return");
        Button saveButton = new Button("Save Game");
        Button mainMenu = new Button("Main Menu");
        Button sound = new Button(AudioManager.getSoundText());
        Button quit = new Button("Quit");

        UIHelper.style(returnButton);
        UIHelper.style(saveButton);
        UIHelper.style(mainMenu);
        UIHelper.style(sound);
        UIHelper.style(quit);

        returnButton.setOnAction(e -> menuStage.close());

        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Game");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Sports Manager Save File", "*.dat")
            );
            fileChooser.setInitialFileName("savegame.dat");

            File file = fileChooser.showSaveDialog(window.getStage());

            if (file != null) {
                try {
                    window.getController().saveGame(file.getAbsolutePath());
                    showInfo("Game Saved", "The game was saved successfully.");
                    menuStage.close();
                } catch (Exception ex) {
                    showError("Save Failed", "The game could not be saved.");
                    ex.printStackTrace();
                }
            }
        });

        mainMenu.setOnAction(e -> {
            AudioManager.stopBGM();
            menuStage.close();
            window.showMainMenu();
        });

        sound.setOnAction(e -> {
            AudioManager.toggleSound();
            sound.setText(AudioManager.getSoundText());
        });

        quit.setOnAction(e -> Platform.exit());

        VBox root = new VBox(15, returnButton, saveButton, mainMenu, sound, quit);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        // MENÜ TASARIMI - LACİVERT GEÇİŞLİ VE NET BEYAZ ÇERÇEVELİ
        root.setStyle(
                "-fx-background-color: rgba(15, 32, 39, 0.95);" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );

        Scene scene = new Scene(root, 280, 380);
        scene.setFill(Color.TRANSPARENT);

        menuStage.setScene(scene);
        menuStage.showAndWait();
    }
    private static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}