package com.team10.ui;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class SportSelectionView {

    private final BorderPane root;

    public SportSelectionView(MainWindow window) {

        root = new BorderPane();

        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);"
        );

        Label title = new Label("SELECT SPORT");

        title.setStyle(
            "-fx-font-size: 36px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: silver;"
        );

        Button football = new Button("Football");

        Button volleyball = new Button("Volleyball");

        UIHelper.style(football);

        UIHelper.style(volleyball);

        volleyball.setDisable(true);

        football.setOnAction(e ->
            startGame(window, new FootballSport())
        );

        HBox sportButtons = new HBox(
            30,
            football,
            volleyball
        );

        sportButtons.setAlignment(Pos.CENTER);

        Rectangle previewBox = new Rectangle(320, 180);

        previewBox.setArcWidth(20);

        previewBox.setArcHeight(20);

        previewBox.setFill(
            Color.rgb(255,255,255,0.10)
        );

        previewBox.setVisible(false);

        Label previewText = new Label(
            "Hover over a sport"
        );

        previewText.setStyle(
            "-fx-text-fill: white;" +
                "-fx-font-size: 18px;"
        );

        football.setOnMouseEntered(e -> {
            previewBox.setVisible(true);
        });

        football.setOnMouseExited(e -> {
            previewBox.setVisible(false);

            previewText.setText(
                "Hover over a sport"
            );
        });

        volleyball.setOnMouseEntered(e -> {
            previewBox.setVisible(true);

            previewText.setText(
                "Coming Soon"
            );
        });

        volleyball.setOnMouseExited(e -> {
            previewBox.setVisible(false);

            previewText.setText(
                "Hover over a sport"
            );
        });

        VBox center = new VBox(
            25,
            title,
            sportButtons,
            previewBox,
            previewText
        );

        center.setAlignment(Pos.CENTER);

        Button menuButton =
            MenuOverlay.createMenuButton(window);

        HBox topBar = new HBox(menuButton);

        topBar.setAlignment(Pos.TOP_RIGHT);

        topBar.setPadding(new Insets(20));

        root.setTop(topBar);

        root.setCenter(center);
    }

    private void startGame(MainWindow window, Sport sport) {

        var teams = List.of(
            com.team10.domain.TestDataFactory.createTeam(),
            com.team10.domain.TestDataFactory.createTeam(),
            com.team10.domain.TestDataFactory.createTeam(),
            com.team10.domain.TestDataFactory.createTeam()
        );

        window.getController().startNewGame(
            sport,
            teams
        );

        window.showLeague();
    }

    public Parent getRoot() {
        return root;
    }
}