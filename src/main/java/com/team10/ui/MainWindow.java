package com.team10.ui;

import com.team10.domain.GameController;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow {

    private final Stage stage;

    private final GameController controller;

    public MainWindow(Stage stage){

        this.stage = stage;

        this.controller = new GameController();

        stage.setTitle("Sports Manager");

        stage.setWidth(900);

        stage.setHeight(600);
    }

    public GameController getController(){
        return controller;
    }

    public Stage getStage() {
        return stage;
    }

    public void showMainMenu(){

        MainMenuView view = new MainMenuView(this);

        stage.setScene(new Scene(view.getRoot()));

        stage.show();
    }

    public void showSportSelection(){

        SportSelectionView view = new SportSelectionView(this);

        stage.setScene(new Scene(view.getRoot()));
    }

    public void showLeague() {

        AudioManager.startBGM();

        LeagueView view = new LeagueView(this);

        stage.setScene(new Scene(view.getRoot()));
    }
}