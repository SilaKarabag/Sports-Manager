package com.team10.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class MainMenuView {

    private final BorderPane root;

    public MainMenuView(MainWindow window) {

        root = new BorderPane();

        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);"
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

        Button sound = new Button(
            AudioManager.getSoundText()        );

        Button quit = new Button("Quit");

        UIHelper.style(start);
        UIHelper.style(sound);
        UIHelper.style(quit);

        start.setOnAction(e ->
            window.showSportSelection()
        );

        sound.setOnAction(e -> {

            AudioManager.toggleSound();

            sound.setText(
                AudioManager.getSoundText()
            );
        });

        quit.setOnAction(e -> Platform.exit());

        VBox menu = new VBox(
            20,
            start,
            sound,
            quit
        );

        menu.setAlignment(Pos.CENTER_RIGHT);

        menu.setPadding(new Insets(0, 60, 0, 0));

        root.setCenter(titleHolder);

        root.setRight(menu);
    }

    public Parent getRoot() {
        return root;
    }
}