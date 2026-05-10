package com.team10.ui;

import com.team10.domain.GameController;
import com.team10.sports.Sport;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow {

    private static final double W = 1100;
    private static final double H = 700;

    private final Stage stage;
    private final GameController controller;

    public MainWindow(Stage stage) {
        this.stage      = stage;
        this.controller = new GameController();
        stage.setTitle("Sports Manager");
        stage.setMinWidth(860);
        stage.setMinHeight(580);
        stage.setResizable(true);
    }

    public GameController getController() { return controller; }
    public Stage          getStage()      { return stage; }

    public Sport getSelectedSport() {
        if (controller.hasActiveSession()) return controller.getSport();
        return null;
    }

    public void showMainMenu() {
        stage.setScene(new Scene(new MainMenuView(this).getRoot(), W, H));
        stage.show();
    }

    public void showSportSelection() {
        stage.setScene(new Scene(new SportSelectionView(this).getRoot(), W, H));
    }

    public void showLeague() {
        AudioManager.startBGM();
        stage.setScene(new Scene(new LeagueView(this).getRoot(), W, H));
    }
}