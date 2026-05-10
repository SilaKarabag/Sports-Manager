package com.team10.ui;

import com.team10.domain.League;
import com.team10.domain.Match;
import com.team10.domain.TeamRecord;
import com.team10.sports.FootballSport;
import com.team10.sports.VolleyballSport;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * BUG FIX 1: Hafta sayısı gösterimi düzeltildi (league.getCurrentWeek() artık doğru)
 * BUG FIX 2: "Season Finished" + nextWeek butonu disable edildi sezon bitince
 * BUG FIX 3: Geçen hafta sonuçları paneli eklendi
 * BUG FIX 4: getSelectedSport() null guard eklendi (load game sonrası)
 * UI: Çok daha zengin ve modern tasarım
 */
public class LeagueView {

    private final StackPane root;
    private final Label weekLabel    = new Label();
    private final Label statusLabel  = new Label();
    private final TableView<TeamRecord> table = new TableView<>();
    private final Button nextWeekBtn = new Button("▶  Play Next Week");
    private final VBox resultsBox    = new VBox(4);

    private static final double ROW_HEIGHT    = 32;
    private static final double HEADER_HEIGHT = 35;

    private final boolean isFootball;
    private final boolean isVolleyball;

    public LeagueView(MainWindow window) {
        // BUG FIX: null guard – load game yapıldıysa sport session'dan gelir
        var sport = window.getSelectedSport();
        isFootball   = sport instanceof FootballSport;
        isVolleyball = sport instanceof VolleyballSport;

        root = new StackPane();

        // Background
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f2027, #203a43, #2c5364);");

        ImageView bg = loadBackground(isFootball ? "/images/football.gif" : "/images/volleyball.jpg");
        if (bg != null) bg.setOpacity(0.18);

        // Top bar
        Button menuBtn = MenuOverlay.createMenuButton(window);
        HBox topBar = new HBox(menuBtn);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(16));

        // Week & status labels
        weekLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 22px; -fx-font-weight: bold;");
        statusLabel.setStyle("-fx-text-fill: #a0c4d8; -fx-font-size: 14px;");

        // Table
        setupTable();

        // Results panel
        setupResultsPanel();

        // Next week button
        UIHelper.style(nextWeekBtn);
        nextWeekBtn.setPrefWidth(220);
        nextWeekBtn.setPrefHeight(42);
        nextWeekBtn.setStyle(nextWeekBtn.getStyle()
                + "-fx-font-size: 15px; -fx-font-weight: bold;");

        nextWeekBtn.setOnAction(e -> {
            var controller = window.getController();
            if (!controller.getLeague().isLeagueFinished()) {
                controller.playNextWeek();
                updateUI(window);
            }
            if (controller.getLeague().isLeagueFinished()) {
                nextWeekBtn.setDisable(true); // BUG FIX: sezon bitti, buton kapat
                new EndScreen(window, window.getController().getLeague().getSortedStandings()).show();
            }
        });

        // Left column: standings
        VBox leftCol = new VBox(10, weekLabel, statusLabel, table, nextWeekBtn);
        leftCol.setAlignment(Pos.TOP_LEFT);
        leftCol.setPadding(new Insets(20, 10, 20, 20));
        leftCol.setPrefWidth(560);

        // Right column: results
        Label resultsTitle = new Label("Last Week Results");
        resultsTitle.setStyle("-fx-text-fill: #a0c4d8; -fx-font-size: 16px; -fx-font-weight: bold;");
        VBox rightCol = new VBox(10, resultsTitle, resultsBox);
        rightCol.setAlignment(Pos.TOP_LEFT);
        rightCol.setPadding(new Insets(20, 20, 20, 10));
        rightCol.setPrefWidth(360);
        rightCol.setStyle(
                "-fx-background-color: rgba(0,0,0,0.35);" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: rgba(255,255,255,0.1);" +
                        "-fx-border-radius: 12;"
        );

        HBox mainContent = new HBox(16, leftCol, rightCol);
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.setPadding(new Insets(0, 16, 16, 0));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(mainContent);

        updateUI(window);

        if (bg != null) root.getChildren().addAll(bg, layout);
        else             root.getChildren().add(layout);
    }

    private void setupTable() {
        String scoredLbl   = isFootball ? "GF" : "Sets W";
        String concededLbl = isFootball ? "GA" : "Sets L";

        TableColumn<TeamRecord, String>  teamCol = col("Team",    320, r -> new SimpleStringProperty(r.getTeam().getName()));
        TableColumn<TeamRecord, Integer> pCol    = intCol("P",     40, r -> r.getMatchesPlayed());
        TableColumn<TeamRecord, Integer> wCol    = intCol("W",     40, r -> r.getWins());
        TableColumn<TeamRecord, Integer> dCol    = intCol("D",     40, r -> r.getDraws());
        TableColumn<TeamRecord, Integer> lCol    = intCol("L",     40, r -> r.getLosses());
        TableColumn<TeamRecord, Integer> gfCol   = intCol(scoredLbl, 55, r -> r.getScored());
        TableColumn<TeamRecord, Integer> gaCol   = intCol(concededLbl, 55, r -> r.getConceded());
        TableColumn<TeamRecord, Integer> ptsCol  = intCol("Pts",  50, r -> r.getPoints());

        table.getColumns().addAll(teamCol, pCol, wCol, dCol, lCol, gfCol, gaCol, ptsCol);

        // BUG FIX: Voleybolda beraberlik kolonu gösterilmez
        if (isVolleyball) dCol.setVisible(false);

        table.setFixedCellSize(ROW_HEIGHT);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMouseTransparent(true);
        table.setFocusTraversable(false);
        table.setStyle(
                "-fx-background-color: rgba(0,0,0,0.55);" +
                        "-fx-control-inner-background: rgba(30,60,80,0.6);" +
                        "-fx-control-inner-background-alt: rgba(20,45,65,0.6);" +
                        "-fx-table-cell-border-color: rgba(255,255,255,0.05);" +
                        "-fx-text-fill: white;"
        );

        // Satır renkleri
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(TeamRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    // İlk 3 → altın/gümüş/bronz tonu
                    int idx = getIndex();
                    if      (idx == 0) setStyle("-fx-background-color: rgba(255,215,0,0.15);");
                    else if (idx == 1) setStyle("-fx-background-color: rgba(192,192,192,0.10);");
                    else if (idx == 2) setStyle("-fx-background-color: rgba(205,127,50,0.10);");
                    else               setStyle("");
                }
            }
        });
    }

    private void setupResultsPanel() {
        resultsBox.setFillWidth(true);
    }

    private void updateUI(MainWindow window) {
        var league = window.getController().getLeague();

        // BUG FIX: Doğru hafta gösterimi
        int displayWeek = Math.min(league.getCurrentWeek(), league.getTotalWeeks());
        weekLabel.setText("Week " + displayWeek + " / " + league.getTotalWeeks());

        boolean finished = league.isLeagueFinished();
        statusLabel.setText(finished ? "🏆 Season Finished!" : "⚽ Season Running");
        nextWeekBtn.setDisable(finished);

        table.getItems().setAll(league.getSortedStandings());
        table.setPrefHeight(HEADER_HEIGHT + table.getItems().size() * ROW_HEIGHT + 2);

        // Geçen hafta sonuçları
        updateResultsPanel(league.getLastWeekMatches());
    }

    private void updateResultsPanel(List<Match> matches) {
        resultsBox.getChildren().clear();
        if (matches.isEmpty()) {
            Label none = new Label("No matches played yet.");
            none.setStyle("-fx-text-fill: #888; -fx-font-size: 13px;");
            resultsBox.getChildren().add(none);
            return;
        }
        for (Match m : matches) {
            String score;
            if (isVolleyball) {
                score = m.getHomeScore() + " - " + m.getAwayScore() + " sets";
            } else {
                score = m.getHomeScore() + " - " + m.getAwayScore();
            }
            Label lbl = new Label(m.getHomeTeam().getName() + "  " + score + "  " + m.getAwayTeam().getName());
            lbl.setStyle("-fx-text-fill: white; -fx-font-size: 12px;" +
                    "-fx-background-color: rgba(255,255,255,0.06); -fx-background-radius:6;" +
                    "-fx-padding: 5 8 5 8;");
            lbl.setMaxWidth(Double.MAX_VALUE);
            resultsBox.getChildren().add(lbl);
        }
    }

    private ImageView loadBackground(String path) {
        try {
            var res = getClass().getResource(path);
            if (res == null) res = getClass().getResource("/images/football.gif");
            if (res == null) return null;
            ImageView iv = new ImageView(new Image(res.toExternalForm()));
            iv.setFitWidth(950);
            iv.setFitHeight(650);
            iv.setPreserveRatio(false);
            return iv;
        } catch (Exception e) { return null; }
    }

    // Helper – generic string column
    private <T> TableColumn<T, String> col(String title, double width,
                                           java.util.function.Function<T, SimpleStringProperty> fn) {
        TableColumn<T, String> c = new TableColumn<>(title);
        c.setPrefWidth(width);
        c.setCellValueFactory(d -> fn.apply(d.getValue()));
        c.setStyle("-fx-alignment: CENTER-LEFT; -fx-text-fill: white;");
        return c;
    }

    // Helper – integer column
    private TableColumn<TeamRecord, Integer> intCol(String title, double width,
                                                    java.util.function.ToIntFunction<TeamRecord> fn) {
        TableColumn<TeamRecord, Integer> c = new TableColumn<>(title);
        c.setPrefWidth(width);
        c.setCellValueFactory(d -> new SimpleIntegerProperty(fn.applyAsInt(d.getValue())).asObject());
        c.setStyle("-fx-alignment: CENTER;");
        return c;
    }

    public Parent getRoot() { return root; }
}
