package com.team10.ui;

import com.team10.domain.*;
import com.team10.sports.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.util.List;

/**
 * YENİ: Kullanıcının yönettiği takımın maçını periyot periyot oynatan dialog.
 * Her periyot sonunda taktik değiştirme imkânı verir.
 * Proje gereği: "player cannot control during play, but between periods can make changes"
 */
public class MatchPlayDialog {

    /**
     * Maçı periyot periyot oynatır. Managed team'in maçı ise interaktif,
     * diğer maçlar otomatik oynatılır.
     */
    public static void playMatchInteractive(MainWindow window, Match match, Team managedTeam) {
        boolean isUserMatch = managedTeam != null &&
                (match.getHomeTeam().equals(managedTeam) || match.getAwayTeam().equals(managedTeam));

        if (!isUserMatch) {
            while (!match.isFinished()) match.playNextQuarter();
            return;
        }

        Stage dialog = new Stage();
        dialog.initOwner(window.getStage());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        Sport sport = match.getSport();
        boolean isVb = sport instanceof VolleyballSport;
        String periodName = isVb ? "Set" : "Half";

        // Skor etiketi
        Label scoreLbl = new Label();
        scoreLbl.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight:bold;");
        updateScore(scoreLbl, match);

        Label periodLbl = new Label(periodName + " 0 / " + sport.getQuarterCount());
        periodLbl.setStyle("-fx-text-fill: #4fc3f7; -fx-font-size: 15px;");

        // Olay logu
        TextArea log = new TextArea();
        log.setEditable(false);
        log.setPrefHeight(200);
        log.setStyle("-fx-control-inner-background: rgba(5,15,25,0.9); -fx-text-fill: #e0e0e0; -fx-font-size:13px;");

        // Taktik seçici
        Label tacticLbl = new Label("Your Tactic:");
        tacticLbl.setStyle("-fx-text-fill: #90caf9; -fx-font-size: 13px;");
        ToggleGroup tg = new ToggleGroup();
        RadioButton atk = new RadioButton("⚔ Attacking");
        RadioButton bal = new RadioButton("⚖ Balanced");
        RadioButton def = new RadioButton("🛡 Defensive");
        for (RadioButton rb : new RadioButton[]{atk, bal, def}) {
            rb.setToggleGroup(tg);
            rb.setStyle("-fx-text-fill:white; -fx-font-size:13px;");
        }
        bal.setSelected(true);

        HBox tacticBox = new HBox(20, atk, bal, def);
        tacticBox.setAlignment(Pos.CENTER);

        // Play button
        Button playBtn = new Button("▶  Play " + periodName);
        UIHelper.style(playBtn);
        playBtn.setPrefWidth(200);

        Label statusLbl = new Label("Press Play to start");
        statusLbl.setStyle("-fx-text-fill:#a0c4d8; -fx-font-size:13px;");

        playBtn.setOnAction(e -> {
            if (match.isFinished()) { dialog.close(); return; }

            // Taktik uygula
            String tactic = atk.isSelected() ? "ATTACKING" : def.isSelected() ? "DEFENSIVE" : "BALANCED";
            managedTeam.setTactic(tactic);

            // Bir periyot oynat
            int prevHome = match.getHomeScore();
            int prevAway = match.getAwayScore();
            match.playNextQuarter();

            updateScore(scoreLbl, match);
            periodLbl.setText(periodName + " " + match.getCurrentQuarter() + " / " + sport.getQuarterCount());

            // Olayları loga ekle
            List<String> events = match.getQuarterEvents();
            if (!events.isEmpty()) {
                String latest = events.get(events.size() - 1);
                log.appendText(latest + "\n");
                // sakatlık uyarıları
                for (int i = events.size() - 1; i >= 0; i--) {
                    if (events.get(i).startsWith("⚠")) {
                        log.appendText(events.get(i) + "\n");
                        break;
                    }
                }
            }

            if (match.isFinished()) {
                String winner;
                if (match.getHomeScore() > match.getAwayScore())
                    winner = match.getHomeTeam().getName() + " wins!";
                else if (match.getAwayScore() > match.getHomeScore())
                    winner = match.getAwayTeam().getName() + " wins!";
                else
                    winner = "Draw!";
                statusLbl.setText("Match finished! " + winner);
                playBtn.setText("✔  Close");
                log.appendText("\n🏁 FINAL: " + match.getHomeTeam().getName() +
                        " " + match.getHomeScore() + " - " + match.getAwayScore() +
                        " " + match.getAwayTeam().getName() + "\n");
            } else {
                statusLbl.setText(periodName + " " + match.getCurrentQuarter() + " done. Ready for next.");
            }
        });

        VBox box = new VBox(14,
                new Label("⚽  MATCH") {{setStyle("-fx-text-fill:#4fc3f7;-fx-font-size:18px;-fx-font-weight:bold;");}},
                scoreLbl, periodLbl, log,
                new Separator(),
                tacticLbl, tacticBox,
                playBtn, statusLbl
        );
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(28));
        box.setStyle("-fx-background-color:rgba(8,18,30,0.98);" +
                "-fx-border-color:rgba(79,195,247,0.55);" +
                "-fx-border-width:1.5;-fx-background-radius:14;-fx-border-radius:14;");

        Scene sc = new Scene(box, 500, 520);
        sc.setFill(Color.TRANSPARENT);
        dialog.setScene(sc);
        dialog.showAndWait();
    }

    private static void updateScore(Label lbl, Match m) {
        lbl.setText(m.getHomeTeam().getName() + "   " +
                m.getHomeScore() + " – " + m.getAwayScore() +
                "   " + m.getAwayTeam().getName());
    }
}
