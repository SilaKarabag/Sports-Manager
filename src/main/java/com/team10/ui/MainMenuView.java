package com.team10.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;

public class MainMenuView {

    private final BorderPane root;

    public MainMenuView(MainWindow window) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0d1117, #161b22, #0f3460);");

        Label title = new Label("SPORTS MANAGER");
        title.setStyle("-fx-font-size: 56px; -fx-font-weight: bold; -fx-text-fill: white;" +
                "-fx-effect: dropshadow(gaussian,#4fc3f7,25,0.6,0,0);");
        Label sub = new Label("League Simulation");
        sub.setStyle("-fx-text-fill: #4fc3f7; -fx-font-size: 18px;");

        VBox titleBox = new VBox(6, title, sub);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setPadding(new Insets(0, 0, 0, 80));

        Button start = new Button("🎮  Start New Game");
        Button load  = new Button("📂  Load Game");
        Button quit  = new Button("✖  Quit");

        UIHelper.style(start);
        UIHelper.style(load);
        UIHelper.style(quit);

        start.setOnAction(e -> window.showSportSelection());

        load.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Load Game");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Save File (*.dat)", "*.dat"));
            File f = fc.showOpenDialog(window.getStage());
            if (f != null) {
                try {
                    window.getController().loadGame(f.getAbsolutePath());
                    window.showLeague();
                } catch (Exception ex) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Load Failed");
                    a.setHeaderText("Load Failed");
                    a.setContentText("Could not load save file:\n" + ex.getMessage());
                    a.showAndWait();
                }
            }
        });

        quit.setOnAction(e -> Platform.exit());

        VBox menu = new VBox(14, start, load, quit);
        menu.setAlignment(Pos.CENTER_RIGHT);
        menu.setPadding(new Insets(0, 80, 0, 0));

        Region divider = new Region();
        divider.setPrefWidth(2);
        divider.setPrefHeight(200);
        divider.setStyle("-fx-background-color: rgba(79,195,247,0.25);");

        HBox center = new HBox(60, titleBox, divider, menu);
        center.setAlignment(Pos.CENTER);

        root.setCenter(center);
    }

    public Parent getRoot() { return root; }
}