package com.team10.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.Alert;

public class MainMenuView {

    private final BorderPane root;

    public MainMenuView(MainWindow window) {

        root = new BorderPane();

        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #3a3f44, #4f7a5e, #3a3f44);"
        );

        Label title = new Label("SPORTS MANAGER");

        title.setStyle(
            "-fx-font-size: 48px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: silver;"
        );

        StackPane titleHolder = new StackPane(title);

        titleHolder.setAlignment(Pos.CENTER);

        Button start = new Button("Start Game");
        Button load = new Button("Load Game");
        Button sound = new Button(AudioManager.getSoundText());
        Button quit = new Button("Quit");

        UIHelper.style(start);
        UIHelper.style(load);
        UIHelper.style(sound);
        UIHelper.style(quit);



        start.setOnAction(e ->
            window.showSportSelection()
        );
        load.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Sports Manager Save File", "*.dat")
            );

            File file = fileChooser.showOpenDialog(window.getStage());

            if (file != null) {
                try {
                    window.getController().loadGame(file.getAbsolutePath());
                    window.showLeague();
                } catch (Exception ex) {
                    showError("Load Failed", "The selected save file could not be loaded.");
                    ex.printStackTrace();
                }
            }
        });

        sound.setOnAction(e -> {

            AudioManager.toggleSound();

            sound.setText(
                AudioManager.getSoundText()
            );
        });

        quit.setOnAction(e -> Platform.exit());

        VBox menu = new VBox(
                start,
                load,
                sound,
                quit
        );

        menu.setAlignment(Pos.CENTER_RIGHT);
        menu.setPadding(new Insets(0, 60, 0, 0));

        root.setCenter(titleHolder);
        root.setRight(menu);
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Parent getRoot() {
        return root;
    }
}