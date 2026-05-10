package com.team10.ui;

import com.team10.domain.*;
import com.team10.sports.*;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.util.List;

public class LeagueView {

    private final BorderPane root;
    private final StackPane  bgLayer;
    private final Label weekLabel   = new Label();
    private final Label statusLabel = new Label();
    private final TableView<TeamRecord> table = new TableView<>();
    private final Button nextWeekBtn = new Button("▶  Play Next Week");
    private final VBox resultsBox   = new VBox(4);
    private final VBox fixtureBox   = new VBox(4);

    private static final double ROW_H = 32;
    private static final double HDR_H = 35;

    private final boolean isFootball;
    private final boolean isVolleyball;

    public LeagueView(MainWindow window) {
        Sport sport = window.getSelectedSport();
        isFootball   = sport instanceof FootballSport;
        isVolleyball = sport instanceof VolleyballSport;

        // Root is BorderPane — fills scene naturally, no binding needed
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right,#0f2027,#203a43,#2c5364);");

        // Background layer
        bgLayer = new StackPane();
        bgLayer.setStyle("-fx-background-color: transparent;");
        ImageView bg = loadBg(isFootball ? "/images/football.gif" : "/images/volleyball.jpg");
        if (bg != null) {
            bg.setOpacity(0.15);
            bg.setPreserveRatio(false);
            bg.fitWidthProperty().bind(root.widthProperty());
            bg.fitHeightProperty().bind(root.heightProperty());
            bgLayer.getChildren().add(bg);
        }

        // Top bar
        Button helpBtn  = HelpDialog.createHelpButton(window);
        Button menuBtn  = MenuOverlay.createMenuButton(window);
        Label  sportTag = new Label(isFootball ? "⚽ FOOTBALL LEAGUE" : "🏐 VOLLEYBALL LEAGUE");
        sportTag.setStyle("-fx-text-fill:#4fc3f7; -fx-font-size:16px; -fx-font-weight:bold;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        HBox topBar = new HBox(8, helpBtn, sportTag, sp, menuBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(12, 16, 4, 16));

        weekLabel.setStyle("-fx-text-fill:white; -fx-font-size:22px; -fx-font-weight:bold;");
        statusLabel.setStyle("-fx-text-fill:#a0c4d8; -fx-font-size:13px;");

        setupTable(window);

        UIHelper.style(nextWeekBtn);
        nextWeekBtn.setPrefWidth(230);
        nextWeekBtn.setPrefHeight(44);
        nextWeekBtn.setOnAction(e -> {
            var ctrl = window.getController();
            if (!ctrl.getLeague().isLeagueFinished()) {
                ctrl.playNextWeek();
                updateUI(window);
            }
            if (ctrl.getLeague().isLeagueFinished()) {
                nextWeekBtn.setDisable(true);
                new EndScreen(window, ctrl.getLeague().getSortedStandings()).show();
            }
        });

        // Left column — takes all spare width
        VBox leftCol = new VBox(10, weekLabel, statusLabel, table, nextWeekBtn);
        leftCol.setAlignment(Pos.TOP_LEFT);
        leftCol.setPadding(new Insets(10, 8, 16, 20));
        HBox.setHgrow(leftCol, Priority.ALWAYS);

        // Right column — fixed width
        Label resTitle = UIHelper.sectionTitle("📋 Last Week Results");
        ScrollPane resSP = scrollPane(resultsBox);
        Label fixTitle = UIHelper.sectionTitle("📅 Upcoming Fixtures");
        ScrollPane fixSP = scrollPane(fixtureBox);

        VBox rightCol = new VBox(10, resTitle, resSP, fixTitle, fixSP);
        rightCol.setPadding(new Insets(12, 14, 14, 10));
        rightCol.setPrefWidth(270);
        rightCol.setMinWidth(200);
        rightCol.setMaxWidth(310);
        rightCol.setStyle("-fx-background-color:rgba(0,0,0,0.38);-fx-background-radius:12;");

        HBox mainContent = new HBox(10, leftCol, rightCol);
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.setPadding(new Insets(0, 10, 10, 0));

        // Content VBox stacked over background
        VBox contentVBox = new VBox(0, topBar, mainContent);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        StackPane centerPane = new StackPane();
        if (bg != null) {
            centerPane.getChildren().addAll(bgLayer, contentVBox);
        } else {
            centerPane.getChildren().add(contentVBox);
        }

        root.setCenter(centerPane);
        updateUI(window);
    }

    private ScrollPane scrollPane(VBox content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setPrefHeight(160);
        sp.setMinHeight(60);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private void setupTable(MainWindow window) {
        String gfLbl = isFootball ? "GF" : "SW";
        String gaLbl = isFootball ? "GA" : "SL";

        TableColumn<TeamRecord, String>  teamCol = new TableColumn<>("Team");
        TableColumn<TeamRecord, Integer> pCol    = new TableColumn<>("P");
        TableColumn<TeamRecord, Integer> wCol    = new TableColumn<>("W");
        TableColumn<TeamRecord, Integer> dCol    = new TableColumn<>("D");
        TableColumn<TeamRecord, Integer> lCol    = new TableColumn<>("L");
        TableColumn<TeamRecord, Integer> gfCol   = new TableColumn<>(gfLbl);
        TableColumn<TeamRecord, Integer> gaCol   = new TableColumn<>(gaLbl);
        TableColumn<TeamRecord, Integer> ptsCol  = new TableColumn<>("Pts");

        teamCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getTeam().getName()));
        teamCol.setPrefWidth(160);
        teamCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setGraphic(null); return; }
                Label lbl = new Label(item);
                lbl.setStyle("-fx-text-fill:#80deea; -fx-underline:true; -fx-cursor:hand;");
                lbl.setOnMouseClicked(e -> {
                    TeamRecord rec = getTableView().getItems().get(getIndex());
                    TeamRosterDialog.show(window, rec.getTeam());
                });
                setGraphic(lbl); setText(null);
            }
        });

        pCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getMatchesPlayed()).asObject());
        wCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getWins()).asObject());
        dCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getDraws()).asObject());
        lCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getLosses()).asObject());
        gfCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getScored()).asObject());
        gaCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getConceded()).asObject());
        ptsCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPoints()).asObject());

        for (TableColumn<?, ?> c : new TableColumn[]{pCol,wCol,dCol,lCol,gfCol,gaCol,ptsCol}) {
            c.setPrefWidth(44);
            c.setStyle("-fx-alignment:CENTER;");
        }

        table.getColumns().addAll(teamCol, pCol, wCol, dCol, lCol, gfCol, gaCol, ptsCol);
        if (isVolleyball) dCol.setVisible(false);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(TeamRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setStyle(""); return; }
                switch (getIndex()) {
                    case 0:  setStyle("-fx-background-color:rgba(255,215,0,0.18);"); break;
                    case 1:  setStyle("-fx-background-color:rgba(192,192,192,0.12);"); break;
                    case 2:  setStyle("-fx-background-color:rgba(205,127,50,0.12);"); break;
                    default: setStyle(""); break;
                }
            }
        });

        table.setFixedCellSize(ROW_H);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFocusTraversable(false);
        table.setStyle(
                "-fx-background-color:rgba(0,0,0,0.55);" +
                        "-fx-control-inner-background:rgba(20,45,65,0.6);" +
                        "-fx-control-inner-background-alt:rgba(15,35,55,0.6);");
    }

    private void updateUI(MainWindow window) {
        League league = window.getController().getLeague();
        boolean finished = league.isLeagueFinished();

        weekLabel.setText("Week " + league.getCurrentWeek() + " / " + league.getTotalWeeks());
        statusLabel.setText(finished
                ? "🏆 Season Finished!"
                : "⏳ Season in Progress  —  click a team name to view roster");
        nextWeekBtn.setDisable(finished);

        table.getItems().setAll(league.getSortedStandings());
        table.setPrefHeight(HDR_H + table.getItems().size() * ROW_H + 2);

        resultsBox.getChildren().clear();
        List<Match> last = league.getLastWeekMatches();
        if (last.isEmpty()) {
            resultsBox.getChildren().add(noData("No matches played yet."));
        } else {
            for (Match m : last) {
                String score = isVolleyball
                        ? m.getHomeScore() + "-" + m.getAwayScore() + " sets"
                        : m.getHomeScore() + "-" + m.getAwayScore();
                resultsBox.getChildren().add(matchRow(
                        m.getHomeTeam().getName(), score, m.getAwayTeam().getName(),
                        m.getHomeScore() > m.getAwayScore(),
                        m.getHomeScore() < m.getAwayScore()));
            }
        }

        fixtureBox.getChildren().clear();
        if (finished) {
            fixtureBox.getChildren().add(noData("Season finished."));
        } else {
            for (Match m : league.getUpcomingMatches()) {
                fixtureBox.getChildren().add(matchRow(
                        m.getHomeTeam().getName(), "vs",
                        m.getAwayTeam().getName(), false, false));
            }
        }
    }

    private Label matchRow(String home, String score, String away, boolean hw, boolean aw) {
        Label l = new Label(home + "  " + score + "  " + away);
        String col = hw ? "rgba(76,175,80,0.15)"
                : aw ? "rgba(244,67,54,0.10)"
                : "rgba(255,255,255,0.06)";
        l.setStyle("-fx-text-fill:white; -fx-font-size:12px;" +
                "-fx-background-color:" + col + ";" +
                "-fx-background-radius:6; -fx-padding:4 8 4 8;");
        l.setMaxWidth(Double.MAX_VALUE);
        return l;
    }

    private Label noData(String msg) {
        Label l = new Label(msg);
        l.setStyle("-fx-text-fill:#607d8b; -fx-font-size:12px;");
        return l;
    }

    private ImageView loadBg(String path) {
        try {
            var r = getClass().getResource(path);
            if (r == null) r = getClass().getResource("/images/football.gif");
            if (r == null) return null;
            return new ImageView(new Image(r.toExternalForm()));
        } catch (Exception e) { return null; }
    }

    public Parent getRoot() { return root; }
}