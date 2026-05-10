package com.team10.ui;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class SportSelectionView {

    private final BorderPane root;

    private ImageView previewGif;

    public SportSelectionView(MainWindow window) {

        root = new BorderPane();

        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #3a3f44, #4f7a5e, #3a3f44);"
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

        football.setOnAction(e ->
            startGame(window, new FootballSport())
        );

        volleyball.setOnAction(e ->
            startGame(window, new VolleyballSport())
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

        previewGif = new ImageView();

        previewGif.setFitWidth(320);
        previewGif.setFitHeight(180);

        previewGif.setPreserveRatio(false);

        previewGif.setVisible(false);

        previewGif.setClip(new Rectangle(320, 180) {{
            setArcWidth(20);
            setArcHeight(20);
        }});

        Image footballGif = new Image(
            getClass().getResourceAsStream(
                "/images/football.gif"
            )
        );


        Image volleyballGif = new Image(
            getClass().getResourceAsStream(
                "/images/volleyball.jpg"
            )
        );

        StackPane previewPane = new StackPane(
            previewBox,
            previewGif
        );

        Label previewText = new Label(
            "Hover over a sport"
        );

        previewText.setStyle(
            "-fx-text-fill: white;" +
                "-fx-font-size: 18px;"
        );

        football.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {

            previewGif.setImage(footballGif);

            previewGif.setVisible(true);

            previewText.setText(
                "Football League"
            );
        });

        football.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {

            previewGif.setVisible(false);

            previewText.setText(
                "Hover over a sport"
            );
        });


        volleyball.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {

            previewGif.setImage(volleyballGif);

            previewGif.setVisible(true);

            previewText.setText(
                "Volleyball League"
            );
        });

        volleyball.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {

            previewGif.setVisible(false);

            previewText.setText(
                "Hover over a sport"
            );
        });

        VBox center = new VBox(
            25,
            title,
            sportButtons,
            previewPane,
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

        window.setSelectedSport(sport);

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