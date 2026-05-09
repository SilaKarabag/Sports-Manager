package com.team10.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
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

        menuStage.initStyle(StageStyle.UNDECORATED);

        Button returnButton =
            new Button("Return");

        Button mainMenu =
            new Button("Main Menu");

        Button sound =
            new Button(
                AudioManager.getSoundText()
            );

        Button quit =
            new Button("Quit");

        UIHelper.style(returnButton);
        UIHelper.style(mainMenu);
        UIHelper.style(sound);
        UIHelper.style(quit);

        returnButton.setOnAction(e ->
            menuStage.close()
        );

        mainMenu.setOnAction(e -> {

            AudioManager.stopBGM();

            menuStage.close();

            window.showMainMenu();
        });

        sound.setOnAction(e -> {

            AudioManager.toggleSound();

            sound.setText(
                AudioManager.getSoundText()
            );
        });

        quit.setOnAction(e ->
            Platform.exit()
        );

        VBox root = new VBox(
            15,
            returnButton,
            mainMenu,
            sound,
            quit
        );

        root.setAlignment(Pos.CENTER);

        root.setPadding(new Insets(25));

        root.setStyle(
            "-fx-background-color: rgba(0,0,0,0.70);" +
                "-fx-border-color: rgba(255,255,255,0.25);" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;"
        );

        Scene scene = new Scene(root, 260, 280);

        scene.setFill(null);

        menuStage.setScene(scene);

        menuStage.showAndWait();
    }
}