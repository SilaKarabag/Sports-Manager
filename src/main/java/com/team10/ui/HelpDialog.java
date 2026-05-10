package com.team10.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class HelpDialog {

    public static Button createHelpButton(MainWindow window) {
        Button b = new Button("?");
        b.setStyle(
                "-fx-background-color: rgba(79,195,247,0.18);" +
                        "-fx-border-color: rgba(79,195,247,0.7);" +
                        "-fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;" +
                        "-fx-text-fill: #4fc3f7; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        b.setPrefWidth(32);
        b.setPrefHeight(32);
        b.setFocusTraversable(false);
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: rgba(79,195,247,0.35);" +
                        "-fx-border-color: #4fc3f7;" +
                        "-fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;" +
                        "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;"));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: rgba(79,195,247,0.18);" +
                        "-fx-border-color: rgba(79,195,247,0.7);" +
                        "-fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;" +
                        "-fx-text-fill: #4fc3f7; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;"));
        b.setOnAction(e -> show(window));
        return b;
    }

    private static void show(MainWindow window) {
        Stage dialog = new Stage();
        dialog.initOwner(window.getStage());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        Label title = new Label("ℹ️  How to Play — Sports Manager");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        ScrollPane scroll = new ScrollPane(buildContent());
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(460);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Button close = new Button("✔  Got it!");
        UIHelper.style(close);
        close.setOnAction(e -> dialog.close());

        VBox box = new VBox(14, title, new Separator(), scroll, close);
        box.setAlignment(Pos.TOP_LEFT);
        box.setPadding(new Insets(28));
        box.setStyle(
                "-fx-background-color: rgba(8,18,32,0.98);" +
                        "-fx-border-color: rgba(79,195,247,0.55);" +
                        "-fx-border-width: 1.5; -fx-background-radius: 14; -fx-border-radius: 14;");

        Scene sc = new Scene(box, 560, 560);
        sc.setFill(Color.TRANSPARENT);
        dialog.setScene(sc);
        dialog.showAndWait();
    }

    private static VBox buildContent() {
        VBox v = new VBox(10);
        v.setPadding(new Insets(4, 8, 4, 4));

        section(v, "🎮  Getting Started",
                "Click 'Start New Game' from the main menu, then choose a sport. " +
                        "4 teams with realistic names and positions are generated automatically. " +
                        "The season uses a full double round-robin — every team plays every other team twice.");

        section(v, "📊  League Table",
                "Teams are ranked by points. Football: Win=3pts, Draw=1pt, Loss=0pts. " +
                        "Volleyball: Win=3pts, Loss=0pts (no draws). " +
                        "Equal points → goal/set difference decides. Top 3 shown in gold/silver/bronze. " +
                        "Click any team name (underlined) to view the full squad roster.");

        section(v, "▶  Playing Weeks",
                "Click 'Play Next Week' to simulate one week of matches. " +
                        "Standings update instantly. The right panel shows last week's results " +
                        "and the upcoming fixtures for the next week.");

        section(v, "👥  Team Roster",
                "Click any team name in the table to open the squad view. " +
                        "Shows each player's Name, Position, Skill (0–100), and injury status. " +
                        "Injured players are shown in red and sit out automatically.");

        section(v, "🤕  Injuries",
                "After each match every player has an 8% chance of injury (1–3 matches). " +
                        "Injured players are excluded from lineups automatically and " +
                        "recover one match per week.");

        section(v, "💾  Save & Load",
                "Open the ☰ menu (top-right) to save or load a game at any time. " +
                        "Save files use the .dat format. All standings, results and injuries " +
                        "are fully restored on load.");

        section(v, "⚽  Football",
                "11 players, 2 halves. Win=3pts, Draw=1pt, Loss=0pts. " +
                        "Tiebreaker: goal difference, then goals scored.");

        section(v, "🏐  Volleyball",
                "6 players, best-of-5 sets (first to win 3 sets wins the match). " +
                        "No draws. Win=3pts, Loss=0pts. Tiebreaker: set difference.");

        section(v, "🏆  End of Season",
                "When all matches are played the champion is announced automatically. " +
                        "Use 'Main Menu' to start a fresh season.");

        return v;
    }

    private static void section(VBox parent, String heading, String body) {
        Label h = new Label(heading);
        h.setStyle("-fx-text-fill: #4fc3f7; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label b = new Label(body);
        b.setStyle("-fx-text-fill: #d0d8e8; -fx-font-size: 12px;");
        b.setWrapText(true);
        b.setMaxWidth(490);
        parent.getChildren().addAll(h, b);
    }
}