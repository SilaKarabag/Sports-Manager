package com.team10.ui;

import com.team10.domain.TeamRecord;
import com.team10.sports.FootballSport;
import com.team10.sports.VolleyballSport;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LeagueView {

    private final StackPane root;

    private final Label weekLabel = new Label();
    private final Label statusLabel = new Label();

    private final TableView<TeamRecord> table = new TableView<>();

    private final Button nextWeek = new Button("Play Next Week");

    private static final double ROW_HEIGHT = 28;
    private static final double HEADER_HEIGHT = 28;

    private final boolean footballMode;
    private final boolean volleyballMode;

    public LeagueView(MainWindow window) {

        footballMode = window.getSelectedSport() instanceof FootballSport;
        volleyballMode = window.getSelectedSport() instanceof VolleyballSport;

        root = new StackPane();
        BorderPane layout = new BorderPane();

        var bg = createBackground(getSportBackground());

        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);"
        );

        weekLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        UIHelper.style(nextWeek);

        setupTable();

        Button menuButton = MenuOverlay.createMenuButton(window);

        HBox topBar = new HBox(menuButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(20));

        nextWeek.setOnAction(e -> {

            var controller = window.getController();

            if (!controller.getLeague().isLeagueFinished()) {
                controller.playNextWeek();
                updateUI(window);
            }

            showEndScreenIfFinished(window);
        });

        VBox content = new VBox(12, weekLabel, statusLabel, table, nextWeek);
        content.setAlignment(Pos.BOTTOM_LEFT);
        content.setPadding(new Insets(20));

        layout.setTop(topBar);
        layout.setCenter(content);

        updateUI(window);

        root.getChildren().addAll(bg, layout);
    }

    private String getSportBackground() {

        if (footballMode) {
            return "/images/football.gif";
        }

        return "/images/volleyball.jpg";
    }

    private StackPane createBackground(String path) {

        StackPane pane = new StackPane();

        var resource = getClass().getResource(path);

        if (resource == null) {
            pane.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);"
            );
            return pane;
        }

        Image img = new Image(resource.toExternalForm());
        ImageView view = new ImageView(img);

        view.setFitWidth(900);
        view.setFitHeight(600);
        view.setPreserveRatio(false);

        pane.getChildren().add(view);

        return pane;
    }

    private void setupTable() {

        String scoredLabel = footballMode ? "GF" : "PF";
        String concededLabel = footballMode ? "GA" : "PA";

        TableColumn<TeamRecord, String> teamCol = new TableColumn<>("Team");
        TableColumn<TeamRecord, Integer> pCol = new TableColumn<>("P");
        TableColumn<TeamRecord, Integer> wCol = new TableColumn<>("W");
        TableColumn<TeamRecord, Integer> dCol = new TableColumn<>("D");
        TableColumn<TeamRecord, Integer> lCol = new TableColumn<>("L");
        TableColumn<TeamRecord, Integer> gfCol = new TableColumn<>(scoredLabel);
        TableColumn<TeamRecord, Integer> gaCol = new TableColumn<>(concededLabel);
        TableColumn<TeamRecord, Integer> ptsCol = new TableColumn<>("Pts");

        teamCol.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getTeam().getName())
        );

        pCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getMatchesPlayed()).asObject()
        );

        wCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getWins()).asObject()
        );

        dCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getDraws()).asObject()
        );

        lCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getLosses()).asObject()
        );

        gfCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getScored()).asObject()
        );

        gaCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getConceded()).asObject()
        );

        ptsCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getPoints()).asObject()
        );

        table.getColumns().addAll(
            teamCol, pCol, wCol, dCol, lCol, gfCol, gaCol, ptsCol
        );

        if (volleyballMode) {
            dCol.setVisible(false);
        }

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(TeamRecord item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("-fx-text-background-color: white;");
            }
        });

        table.setFixedCellSize(ROW_HEIGHT);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMouseTransparent(true);
        table.setFocusTraversable(false);

        table.setStyle(
            "-fx-background-color: rgba(0,0,0,0.55);" +
                "-fx-control-inner-background: rgba(255,255,255,0.10);" +
                "-fx-control-inner-background-alt: rgba(255,255,255,0.08);" +
                "-fx-table-cell-border-color: rgba(0,0,0,0.25);" +
                "-fx-table-header-border-color: rgba(255,255,255,0.25);"
        );
    }

    private void updateUI(MainWindow window) {

        var league = window.getController().getLeague();

        weekLabel.setText(
            "Week: " + Math.min(
                league.getCurrentWeek(),
                league.getTeams().size() * 2
            )
        );

        statusLabel.setText(
            league.isLeagueFinished()
                ? "Season Finished"
                : "Season Running"
        );

        table.getItems().setAll(league.getSortedStandings());

        table.setPrefHeight(
            HEADER_HEIGHT + table.getItems().size() * ROW_HEIGHT
        );
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

    public Parent getRoot() {
        return root;
    }
}