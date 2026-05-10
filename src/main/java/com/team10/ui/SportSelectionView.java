package com.team10.ui;

import com.team10.sports.FootballSport;
import com.team10.sports.VolleyballSport; // VolleyballSport importu eklendi
import com.team10.sports.Sport;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class SportSelectionView {

    private final BorderPane root;
    private ImageView previewGif;

    public SportSelectionView(MainWindow window) {

        root = new BorderPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);"
        );

        Label title = new Label("SELECT SPORT");

        title.setStyle(
                "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: silver;"
        );

        Button football = new Button("Football");
        Button volleyball = new Button("Volleyball");

        // VOLEYBOL AKTIF EDILDI
        volleyball.setDisable(false);

        UIHelper.style(football);
        UIHelper.style(volleyball);

        football.setOnAction(e ->
                startGame(window, new FootballSport())
        );

        // VOLEYBOL BUTONUNA AKSIYON EKLENDI
        volleyball.setOnAction(e ->
                startGame(window, new VolleyballSport())
        );

        HBox sportButtons = new HBox(
                30,
                football,
                volleyball
        );

        sportButtons.setAlignment(Pos.CENTER);

        Rectangle previewBox = new Rectangle(320, 180);
        previewBox.setArcWidth(20);
        previewBox.setArcHeight(20);
        previewBox.setFill(Color.rgb(255,255,255,0.10));

        previewGif = new ImageView();
        previewGif.setFitWidth(320);
        previewGif.setFitHeight(180);
        previewGif.setPreserveRatio(false);
        previewGif.setVisible(false);

        previewGif.setClip(new Rectangle(320, 180) {{
            setArcWidth(20);
            setArcHeight(20);
        }});

        // GÖRSELLER YÜKLENIYOR
        Image footballGif = null;
        Image volleyballGif = null;
        try {
            footballGif = new Image(getClass().getResourceAsStream("/images/football.gif"));
            // Voleybol için projenizdeki mevcut imajı kullanıyoruz
            volleyballGif = new Image(getClass().getResourceAsStream("/images/volleyball.jpg"));
        } catch (Exception e) {
            System.out.println("Görseller yüklenirken hata oluştu, yolları kontrol et!");
        }

        final Image finalFootball = footballGif;
        final Image finalVolleyball = volleyballGif;

        StackPane previewPane = new StackPane(
                previewBox,
                previewGif
        );

        Label previewText = new Label("Hover over a sport");
        previewText.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        // FOOTBALL HOVER EFEKTLERI
        football.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            if (finalFootball != null) previewGif.setImage(finalFootball);
            previewGif.setVisible(true);
            previewText.setText("Football League");
        });

        football.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            previewGif.setVisible(false);
            previewText.setText("Hover over a sport");
        });

        // VOLLEYBALL HOVER EFEKTLERI AKTIF EDILDI
        volleyball.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            if (finalVolleyball != null) previewGif.setImage(finalVolleyball);
            previewGif.setVisible(true);
            previewText.setText("Volleyball League");
        });

        volleyball.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            previewGif.setVisible(false);
            previewText.setText("Hover over a sport");
        });

        VBox center = new VBox(
                25,
                title,
                sportButtons,
                previewPane,
                previewText
        );
        center.setAlignment(Pos.CENTER);

        Button menuButton = MenuOverlay.createMenuButton(window);
        HBox topBar = new HBox(menuButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(20));

        root.setTop(topBar);
        root.setCenter(center);
    }

    private void startGame(MainWindow window, Sport sport) {
        var teams = List.of(
                com.team10.domain.TestDataFactory.createTeam(),
                com.team10.domain.TestDataFactory.createTeam(),
                com.team10.domain.TestDataFactory.createTeam(),
                com.team10.domain.TestDataFactory.createTeam()
        );

        window.getController().startNewGame(sport, teams);
        window.showLeague();
    }

    public Parent getRoot() {
        return root;
    }
}