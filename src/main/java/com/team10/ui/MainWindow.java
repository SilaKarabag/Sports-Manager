package com.team10.ui;

import com.team10.domain.GameController;
import com.team10.sports.Sport;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * BUG FIX 1: selectedSport MainWindow'da tutuluyordu. Ama GameSession zaten
 *   sport'u içeriyor. Load game yapıldığında selectedSport null kalıyordu
 *   → LeagueView crash. Düzeltme: getSelectedSport() controller'dan alıyor.
 *
 * BUG FIX 2: showLeague() çağrısından önce AudioManager.startBGM() doğru sırada.
 *
 * BUG FIX 3: Scene boyutları sabit. Tüm view'larda aynı boyut zorunlu.
 */
public class MainWindow {

    private final Stage stage;
    private final GameController controller;

    // BUG FIX: selectedSport sadece yeni oyun başlatılırken set edilir.
    // Load sonrası session'dan alınır.
    private Sport _selectedSport;

    public MainWindow(Stage stage) {
        this.stage = stage;
        this.controller = new GameController();
        stage.setTitle("Sports Manager");
        stage.setWidth(950);
        stage.setHeight(650);
        stage.setResizable(false);
    }

    public GameController getController() { return controller; }
    public Stage          getStage()      { return stage; }

    /** BUG FIX: Load sonrası sport, session'dan gelir */
    public Sport getSelectedSport() {
        if (controller.hasActiveSession()) {
            return controller.getSport();
        }
        return _selectedSport;
    }

    public void setSelectedSport(Sport sport) {
        this._selectedSport = sport;
    }

    public void showMainMenu() {
        MainMenuView view = new MainMenuView(this);
        stage.setScene(new Scene(view.getRoot(), 950, 650));
        stage.show();
    }

    public void showSportSelection() {
        SportSelectionView view = new SportSelectionView(this);
        stage.setScene(new Scene(view.getRoot(), 950, 650));
    }

    public void showLeague() {
        AudioManager.startBGM();
        LeagueView view = new LeagueView(this);
        stage.setScene(new Scene(view.getRoot(), 950, 650));
    }
}
