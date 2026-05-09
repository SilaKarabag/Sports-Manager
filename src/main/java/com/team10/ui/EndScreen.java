package com.team10.ui;

import com.team10.domain.TeamRecord;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
                "-fx-text-fill: silver;"
        );

        Label winnerLabel = new Label(
            "Winner: " + winner.getTeam().getName()
        );

        winnerLabel.setStyle(
            "-fx-font-size: 18px;" +
                "-fx-text-fill: white;"
        );

        Button returnLeague = new Button("Return to League");
        Button mainMenu = new Button("Main Menu");
        Button quit = new Button("Quit");

        UIHelper.style(returnLeague);
        UIHelper.style(mainMenu);
        UIHelper.style(quit);

        returnLeague.setOnAction(e -> stage.close());

        mainMenu.setOnAction(e -> {
            stage.close();
            Platform.runLater(window::showMainMenu);
        });

        quit.setOnAction(e -> Platform.exit());

        VBox root = new VBox(
            18,
            title,
            winnerLabel,
            returnLeague,
            mainMenu,
            quit
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.setStyle(
            "-fx-background-color: rgba(22,22,22,0.97);" +
                "-fx-border-color: rgba(192,192,192,0.45);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 6;"
        );

        stage.setScene(new Scene(root, 340, 320));
    }

    public void show() {
        stage.show();
    }
}