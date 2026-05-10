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

/**
 * BUG FIX 1: standings boş veya null gelebilir → IndexOutOfBoundsException.
 *   Null guard eklendi.
 *
 * BUG FIX 2: scene.setFill(null) şeffaf köşeler için JavaFX'te sadece
 *   TRANSPARENT stage ile çalışıyor. StageStyle.TRANSPARENT kullanıldı.
 *
 * BUG FIX 3: UI – tam puan tablosu (top 3) gösterildi.
 */
public class EndScreen {

    private final Stage stage;

    public EndScreen(MainWindow window, List<TeamRecord> standings) {
        stage = new Stage();
        stage.initOwner(window.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT); // BUG FIX: şeffaf köşeler

        // BUG FIX: null / boş guard
        if (standings == null || standings.isEmpty()) {
            buildErrorScene("No standings available.");
            return;
        }

        TeamRecord winner = standings.get(0);

        Label title = new Label("🏆  SEASON COMPLETE");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Kupa görseli
        ImageView trophy = new ImageView();
        try {
            var res = getClass().getResource("/images/trophy.png");
            if (res != null) trophy.setImage(new Image(res.toExternalForm()));
        } catch (Exception ignored) {}
        trophy.setFitWidth(120); trophy.setPreserveRatio(true);

        Label winnerLabel = new Label("🥇  " + winner.getTeam().getName());
        winnerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: gold;");

        Label stats = new Label(
                winner.getMatchesPlayed() + " games · " +
                        winner.getWins() + "W  " + winner.getDraws() + "D  " + winner.getLosses() + "L  · " +
                        winner.getPoints() + " pts"
        );
        stats.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13px;");

        // Top 3
        VBox podium = new VBox(4);
        String[] medals = {"🥇", "🥈", "🥉"};
        for (int i = 0; i < Math.min(3, standings.size()); i++) {
            TeamRecord r = standings.get(i);
            Label row = new Label(medals[i] + "  " + r.getTeam().getName() + "  —  " + r.getPoints() + " pts");
            row.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            podium.getChildren().add(row);
        }
        podium.setAlignment(Pos.CENTER);

        Button returnLeague = new Button("View Standings");
        Button mainMenu     = new Button("Main Menu");
        Button quit         = new Button("Quit");
        UIHelper.style(returnLeague); UIHelper.style(mainMenu); UIHelper.style(quit);

        returnLeague.setOnAction(e -> { stage.close(); window.showLeague(); });
        mainMenu.setOnAction(e    -> { stage.close(); AudioManager.stopBGM(); window.showMainMenu(); });
        quit.setOnAction(e        -> Platform.exit());

        VBox content = new VBox(18, title, trophy, winnerLabel, stats, podium, returnLeague, mainMenu, quit);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(32));
        content.setStyle(
                "-fx-background-color: rgba(10,20,30,0.97);" +
                        "-fx-border-color: gold; -fx-border-width: 2.5;" +
                        "-fx-border-radius: 16; -fx-background-radius: 16;"
        );

        Scene scene = new Scene(content, 380, 540);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.setScene(scene);
    }

    private void buildErrorScene() {
        buildErrorScene("An error occurred.");
    }

    private void buildErrorScene(String msg) {
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        VBox box = new VBox(lbl); box.setAlignment(Pos.CENTER); box.setPadding(new Insets(20));
        stage.setScene(new Scene(box, 300, 100));
    }

    public void show() {
        AudioManager.stopBGM();
        AudioManager.playSFX("/audio/winner.mp3");
        stage.showAndWait();
    }
}
