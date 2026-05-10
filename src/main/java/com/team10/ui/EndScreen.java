package com.team10.ui;

import com.team10.domain.TeamRecord;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.List;

public class EndScreen {

    private final Stage stage;

    public EndScreen(MainWindow window, List<TeamRecord> standings) {
        stage = new Stage();
        stage.initOwner(window.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        // Şampiyonu al
        TeamRecord winner = standings.get(0);

        Label title = new Label("SEASON FINISHED");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Kupa görselini hata korumalı yükle
        ImageView trophy = new ImageView();
        try {
            var resource = getClass().getResource("/images/trophy.png");
            if (resource != null) {
                trophy.setImage(new Image(resource.toExternalForm()));
            }
        } catch (Exception e) {
            System.out.println("Trophy image could not be loaded, skipping visual.");
        }

        trophy.setFitWidth(150);
        trophy.setPreserveRatio(true);

        Label winnerLabel = new Label("Champion: " + winner.getTeam().getName());
        winnerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: gold;");

        Button returnLeague = new Button("Return to League");
        Button mainMenu = new Button("Main Menu");
        Button quit = new Button("Quit");

        // Senin UIHelper sınıfındaki metodları kullanıyoruz
        UIHelper.style(returnLeague);
        UIHelper.style(mainMenu);
        UIHelper.style(quit);

        returnLeague.setOnAction(e -> {
            stage.close();
            window.showLeague();
        });

        mainMenu.setOnAction(e -> {
            stage.close();
            AudioManager.stopBGM();
            window.showMainMenu();
        });

        quit.setOnAction(e -> Platform.exit());

        VBox root = new VBox(20, title, trophy, winnerLabel, returnLeague, mainMenu, quit);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        // Şık bir bitiş paneli tasarımı
        root.setStyle(
                "-fx-background-color: rgba(20, 20, 20, 0.95);" +
                        "-fx-border-color: gold;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;"
        );

        Scene scene = new Scene(root, 360, 500);
        scene.setFill(null); // Köşelerin oval görünmesi için arkaplanı şeffaf yapıyoruz
        stage.setScene(scene);
    }

    public void show() {
        // Müzik yönetimi
        AudioManager.stopBGM();
        AudioManager.playSFX("/audio/winner.mp3");
        stage.showAndWait();
    }
}