package com.team10.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        MainWindow window = new MainWindow(stage);
        window.showMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
