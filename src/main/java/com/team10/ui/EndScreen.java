package com.team10.ui;

import com.team10.domain.TeamRecord;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;
import java.util.List;

public class EndScreen {

    private final Stage stage;

    public EndScreen(MainWindow window, List<TeamRecord> standings) {
        stage = new Stage();
        stage.initOwner(window.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT); // FIX: UNDECORATED → TRANSPARENT

        if (standings == null || standings.isEmpty()) {
            buildError(); return;
        }

        TeamRecord winner = standings.get(0);

        Label title = new Label("🏆  SEASON COMPLETE");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        ImageView trophy = new ImageView();
        try {
            var res = getClass().getResource("/images/trophy.png");
            if (res != null) trophy.setImage(new Image(res.toExternalForm()));
        } catch (Exception ignored) {}
        trophy.setFitWidth(110); trophy.setPreserveRatio(true);

        Label winnerLbl = new Label("🥇  " + winner.getTeam().getName());
        winnerLbl.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: gold;");

        Label stats = new Label(
                "P:" + winner.getMatchesPlayed() + "  W:" + winner.getWins() +
                        "  D:" + winner.getDraws() + "  L:" + winner.getLosses() +
                        "  Pts:" + winner.getPoints()
        );
        stats.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13px;");

        // Top 3 podyum
        VBox podium = new VBox(5);
        String[] medals = {"🥇","🥈","🥉"};
        for (int i = 0; i < Math.min(3, standings.size()); i++) {
            TeamRecord r = standings.get(i);
            Label row = new Label(medals[i] + "  " + r.getTeam().getName() + "  —  " + r.getPoints() + " pts");
            row.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            podium.getChildren().add(row);
        }
        podium.setAlignment(Pos.CENTER);

        Button viewStandings = new Button("📊  View Standings");
        Button mainMenu      = new Button("🏠  Main Menu");
        Button quit          = new Button("✖  Quit");
        UIHelper.style(viewStandings);
        UIHelper.style(mainMenu);
        UIHelper.style(quit);

        viewStandings.setOnAction(e -> { stage.close(); window.showLeague(); });
        mainMenu.setOnAction(e -> { stage.close(); AudioManager.stopBGM(); window.showMainMenu(); });
        quit.setOnAction(e -> Platform.exit());

        VBox box = new VBox(16, title, trophy, winnerLbl, stats, new Separator(), podium, viewStandings, mainMenu, quit);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: rgba(8,18,30,0.98);" +
                "-fx-border-color: gold; -fx-border-width: 2.5;" +
                "-fx-border-radius: 16; -fx-background-radius: 16;");

        Scene sc = new Scene(box, 380, 560);
        sc.setFill(Color.TRANSPARENT);
        stage.setScene(sc);
    }

    private void buildError() {
        Label l = new Label("No standings available.");
        l.setStyle("-fx-text-fill: red;");
        stage.setScene(new Scene(new VBox(l), 300, 100));
    }

    public void show() {
        AudioManager.stopBGM();
        AudioManager.playSFX("/audio/winner.mp3");
        stage.showAndWait();
    }
}
