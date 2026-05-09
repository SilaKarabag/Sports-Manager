package com.team10.ui;

import com.team10.domain.TeamRecord;
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

    //private final BorderPane root;
    private final StackPane root;

    private final Label weekLabel = new Label();

    private final Label statusLabel = new Label();

    private final TableView<TeamRecord> table =
        new TableView<>();

    private final Button nextWeek =
        new Button("Play Next Week");

    private static final double ROW_HEIGHT = 28;

    private static final double HEADER_HEIGHT = 28;

    public LeagueView(MainWindow window) {

        //will add background image

        //AudioManager.startBGM();

        ImageView bg = createBackground("/images/football.gif");

        //root = new BorderPane();
        root = new StackPane();

        BorderPane layout = new BorderPane();

        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);"
        );

        // ONLY these labels are white
        weekLabel.setStyle(
            "-fx-text-fill: white;" +
                "-fx-font-size: 18px;"
        );

        statusLabel.setStyle(
            "-fx-text-fill: white;" +
                "-fx-font-size: 16px;"
        );

        UIHelper.style(nextWeek);

        setupTable();

        Button menuButton =
            MenuOverlay.createMenuButton(window);

        HBox topBar = new HBox(menuButton);

        topBar.setAlignment(Pos.TOP_RIGHT);

        topBar.setPadding(new Insets(20));

        nextWeek.setOnAction(e -> {

            var controller = window.getController();

            if (controller.getLeague().isLeagueFinished()) {

                nextWeek.setDisable(true);

                new EndScreen(
                    window,
                    controller.getLeague()
                        .getSortedStandings()
                ).show();

                return;
            }

            controller.playNextWeek();

            updateUI(window);

            if (controller.getLeague().isLeagueFinished()) {

                nextWeek.setDisable(true);

                new EndScreen(
                    window,
                    controller.getLeague()
                        .getSortedStandings()
                ).show();
            }
        });

        VBox content = new VBox(
            12,
            weekLabel,
            statusLabel,
            table,
            nextWeek
        );

        content.setPadding(new Insets(20));

        layout.setTop(topBar);
        layout.setCenter(content);

        updateUI(window);

        root.getChildren().addAll(bg, layout);


    }

    private void setupTable() {

        TableColumn<TeamRecord, String> teamCol =
            new TableColumn<>("Team");

        TableColumn<TeamRecord, Integer> pCol =
            new TableColumn<>("P");

        TableColumn<TeamRecord, Integer> wCol =
            new TableColumn<>("W");

        TableColumn<TeamRecord, Integer> dCol =
            new TableColumn<>("D");

        TableColumn<TeamRecord, Integer> lCol =
            new TableColumn<>("L");

        TableColumn<TeamRecord, Integer> gfCol =
            new TableColumn<>("GF");

        TableColumn<TeamRecord, Integer> gaCol =
            new TableColumn<>("GA");

        TableColumn<TeamRecord, Integer> ptsCol =
            new TableColumn<>("Pts");

        teamCol.setCellValueFactory(d ->
            new SimpleStringProperty(
                d.getValue().getTeam().getName()
            )
        );

        pCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(
                d.getValue().getMatchesPlayed()
            ).asObject()
        );

        wCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(
                d.getValue().getWins()
            ).asObject()
        );

        dCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(
                d.getValue().getDraws()
            ).asObject()
        );

        lCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(
                d.getValue().getLosses()
            ).asObject()
        );

        gfCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(
                d.getValue().getScored()
            ).asObject()
        );

        gaCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(
                d.getValue().getConceded()
            ).asObject()
        );

        ptsCol.setCellValueFactory(d ->
            new SimpleIntegerProperty(
                d.getValue().getPoints()
            ).asObject()
        );

        table.getColumns().addAll(
            teamCol,
            pCol,
            wCol,
            dCol,
            lCol,
            gfCol,
            gaCol,
            ptsCol
        );

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(TeamRecord item, boolean empty) {
                super.updateItem(item, empty);

                setStyle("-fx-text-background-color: white;");
            }
        });

        table.setFixedCellSize(ROW_HEIGHT);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);" +
                "-fx-control-inner-background: rgba(255,255,255,0.05);" +
                "-fx-control-inner-background-alt: rgba(255,255,255,0.03);" +
                "-fx-table-header-border-color: rgba(0,0,0,0.4);" +
                "-fx-table-cell-border-color: rgba(0,0,0,0.25);" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        table.setMouseTransparent(true);
        table.setSelectionModel(null);
    }

    private void updateUI(MainWindow window) {

        var league = window.getController().getLeague();

        weekLabel.setText(
            "Week: " + league.getCurrentWeek()
        );

        statusLabel.setText(
            league.isLeagueFinished()
                ? "Season Finished"
                : "Season Running"
        );

        table.getItems().setAll(
            league.getSortedStandings()
        );

        table.setPrefHeight(
            HEADER_HEIGHT +
                table.getItems().size() * ROW_HEIGHT
        );
    }

    public Parent getRoot() {
        return root;
    }

    private ImageView createBackground(String path) {

        Image image = new Image(
            getClass().getResource(path).toExternalForm()
        );

        ImageView bg = new ImageView(image);

        bg.setFitWidth(900);
        bg.setFitHeight(600);
        bg.setPreserveRatio(false);

        return bg;
    }

}