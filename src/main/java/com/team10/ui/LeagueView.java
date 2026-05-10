package com.team10.ui;

import com.team10.domain.TeamRecord;
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

public class LeagueView {

    private final StackPane root;
    private final Label weekLabel = new Label();
    private final Label statusLabel = new Label();
    private final TableView<TeamRecord> table = new TableView<>();
    private final Button nextWeek = new Button("Play Next Week");

    private static final double ROW_HEIGHT = 30;
    private static final double HEADER_HEIGHT = 32;

    public LeagueView(MainWindow window) {
        var controller = window.getController();
        var sport = controller.getSession().getSport();

        String bgPath = (sport instanceof VolleyballSport) ? "/images/volleyball.jpg" : "/images/football.gif";
        ImageView bg = createBackground(bgPath);

        root = new StackPane();
        BorderPane layout = new BorderPane();

        root.setStyle("-fx-background-color: #0f2027;");

        weekLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        statusLabel.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 14px;");

        UIHelper.style(nextWeek);
        setupTable();

        Button menuButton = MenuOverlay.createMenuButton(window);
        HBox topBar = new HBox(menuButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(20));

        nextWeek.setOnAction(e -> {
            if (controller.getLeague().isLeagueFinished()) {
                showEndScreen(window);
                return;
            }
            controller.playNextWeek();
            updateUI(window);
            if (controller.getLeague().isLeagueFinished()) {
                showEndScreen(window);
            }
        });

        VBox content = new VBox(15, weekLabel, statusLabel, table, nextWeek);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setMaxWidth(800);

        layout.setTop(topBar);
        layout.setCenter(content);

        updateUI(window);
        root.getChildren().addAll(bg, layout);
    }

    private void showEndScreen(MainWindow window) {
        nextWeek.setDisable(true);
        new EndScreen(window, window.getController().getLeague().getSortedStandings()).show();
    }

    private void setupTable() {
        TableColumn<TeamRecord, String> teamCol = new TableColumn<>("Team");
        TableColumn<TeamRecord, Integer> pCol = new TableColumn<>("P");
        TableColumn<TeamRecord, Integer> wCol = new TableColumn<>("W");
        TableColumn<TeamRecord, Integer> dCol = new TableColumn<>("D");
        TableColumn<TeamRecord, Integer> lCol = new TableColumn<>("L");
        TableColumn<TeamRecord, Integer> diffCol = new TableColumn<>("Diff");
        TableColumn<TeamRecord, Integer> ptsCol = new TableColumn<>("Pts");

        teamCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTeam().getName()));
        pCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getMatchesPlayed()).asObject());
        wCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getWins()).asObject());
        dCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getDraws()).asObject());
        lCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getLosses()).asObject());
        diffCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getGoalDifference()).asObject());
        ptsCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPoints()).asObject());

        table.getColumns().addAll(teamCol, pCol, wCol, dCol, lCol, diffCol, ptsCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(ROW_HEIGHT);
        table.setMouseTransparent(true);
        table.setSelectionModel(null);

        // TABLO TASARIMI: Arka planı açık gri/beyaz yaptık ki siyah yazılar parlasın
        table.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-control-inner-background: rgba(245, 245, 245, 0.9);" +
                        "-fx-control-inner-background-alt: rgba(230, 230, 230, 0.9);" +
                        "-fx-table-cell-border-color: #bbbbbb;" +
                        "-fx-table-header-border-color: #bbbbbb;"
        );

        // YAZI RENGİNİ SİYAH YAPMAK İÇİN KRİTİK DÜZENLEME
        table.setRowFactory(tv -> new TableRow<TeamRecord>() {
            @Override
            protected void updateItem(TeamRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    // -fx-text-fill SİYAH yapar, font-weight KALIN yapar.
                    setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void updateUI(MainWindow window) {
        var league = window.getController().getLeague();
        int totalWeeks = (league.getSortedStandings().size() * 2) - 2;
        weekLabel.setText("Week: " + (league.getCurrentWeek() - 1) + " / " + totalWeeks);
        statusLabel.setText(league.isLeagueFinished() ? "Status: Season Finished" : "Status: Season Running");
        table.getItems().setAll(league.getSortedStandings());
        table.setPrefHeight(HEADER_HEIGHT + (table.getItems().size() * ROW_HEIGHT) + 5);
    }

    public Parent getRoot() { return root; }

    private ImageView createBackground(String path) {
        try {
            var resource = getClass().getResource(path);

            if (resource == null) {
                resource = getClass().getResource("/images/football.gif");
            }

            if (resource == null) {
                return new ImageView();
            }

            Image image = new Image(resource.toExternalForm());
            ImageView bg = new ImageView(image);
            bg.setFitWidth(900);
            bg.setFitHeight(600);
            bg.setPreserveRatio(false);
            bg.setOpacity(0.4);
            return bg;
        } catch (Exception e) {
            return new ImageView();
        }
    }
}