package com.team10.ui;

import com.team10.domain.TeamRecord;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class EndScreen {

    private final Stage stage;

    public EndScreen(
        MainWindow window,
        List<TeamRecord> standings
    ) {

        stage = new Stage();

        stage.initOwner(window.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        TeamRecord winner = standings.get(0);

        Label title = new Label("SEASON FINISHED");
        title.setStyle(
            "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        ImageView trophy = new ImageView(
            new Image(
                EndScreen.class.getResource("/images/trophy.png").toExternalForm()
            )
        );

        trophy.setFitWidth(120);
        trophy.setFitHeight(120);
        trophy.setPreserveRatio(true);

        Label winnerLabel = new Label(
            "Winner: " + winner.getTeam().getName()
        );

        winnerLabel.setStyle(
            "-fx-font-size: 18px;" +
                "-fx-text-fill: gold;"
        );

        Button returnLeague = new Button("Return to League");
        Button mainMenu = new Button("Main Menu");
        Button quit = new Button("Quit");

        UIHelper.style(returnLeague);
        UIHelper.style(mainMenu);
        UIHelper.style(quit);

        returnLeague.setOnAction(e -> {stage.close(); window.showLeague();});

        mainMenu.setOnAction(e -> {
            stage.close();
            Platform.runLater(window::showMainMenu);
        });

        quit.setOnAction(e -> Platform.exit());

        VBox root = new VBox(
            18,
            title,
            trophy,
            winnerLabel,
            returnLeague,
            mainMenu,
            quit
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.setStyle(
            "-fx-background-color: rgba(22,22,22,0.97);" +
                "-fx-border-color: gold;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;"
        );

        stage.setScene(new Scene(root, 340, 450));
    }

    public void show() {

        AudioManager.stopBGM();
        AudioManager.playSFX("/audio/winner.mp3");

        stage.show();
    }
}