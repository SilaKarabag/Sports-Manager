package com.team10.ui;

import com.team10.domain.TestDataFactory;
import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;
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

import java.util.ArrayList;
import java.util.List;

/**
 * BUG FIX 1: Orijinal startGame(), TestDataFactory.createTeam() çağırıyordu
 *   ama bu metod her zaman FootballSport için kadro oluşturuyordu.
 *   Voleybol seçilince 6 kişilik kadro yerine 15 kişilik futbol kadrosu geliyordu.
 *   FIX: TestDataFactory.createTeam(sport) kullanıldı.
 *
 * BUG FIX 2: 4 takım hardcode. 4 takım korundu (proje şartı karşılanıyor).
 *
 * BUG FIX 3: TestDataFactory.reset() çağrılmıyor, takım isimleri biriküyor.
 *   FIX: startGame() başında reset() çağrılıyor.
 */
public class SportSelectionView {

    private final BorderPane root;
    private ImageView previewGif;

    public SportSelectionView(MainWindow window) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e, #0f3460);");

        Label title = new Label("SELECT YOUR SPORT");
        title.setStyle(
                "-fx-font-size: 40px; -fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-effect: dropshadow(gaussian, #4fc3f7, 20, 0.5, 0, 0);"
        );

        Button football   = new Button("⚽  Football");
        Button volleyball = new Button("🏐  Volleyball");

        styleSportButton(football,   "#1565c0", "#42a5f5");
        styleSportButton(volleyball, "#6a1b9a", "#ce93d8");

        football.setOnAction(e   -> startGame(window, new FootballSport()));
        volleyball.setOnAction(e -> startGame(window, new VolleyballSport()));

        HBox sportButtons = new HBox(40, football, volleyball);
        sportButtons.setAlignment(Pos.CENTER);

        // Preview area
        Rectangle previewBox = new Rectangle(360, 200);
        previewBox.setArcWidth(20); previewBox.setArcHeight(20);
        previewBox.setFill(Color.rgb(255, 255, 255, 0.06));
        previewBox.setStroke(Color.rgb(255, 255, 255, 0.2));

        previewGif = new ImageView();
        previewGif.setFitWidth(360); previewGif.setFitHeight(200);
        previewGif.setPreserveRatio(false);
        previewGif.setVisible(false);
        previewGif.setClip(new Rectangle(360, 200) {{ setArcWidth(20); setArcHeight(20); }});

        Image footballGif   = loadImage("/images/football.gif");
        Image volleyballGif = loadImage("/images/volleyball.jpg");

        Label previewText = new Label("Hover over a sport to preview");
        previewText.setStyle("-fx-text-fill: #90caf9; -fx-font-size: 14px;");

        addHover(football,   footballGif,   previewText, "Football League – 11 vs 11, 2 Halves");
        addHover(volleyball, volleyballGif, previewText, "Volleyball League – 6 vs 6, Best of 5 Sets");

        StackPane previewPane = new StackPane(previewBox, previewGif);

        VBox center = new VBox(30, title, sportButtons, previewPane, previewText);
        center.setAlignment(Pos.CENTER);

        Button menuBtn = MenuOverlay.createMenuButton(window);
        HBox topBar = new HBox(menuBtn);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(16));

        root.setTop(topBar);
        root.setCenter(center);
    }

    private void startGame(MainWindow window, Sport sport) {
        // BUG FIX: reset sayaçları, doğru sporu geç
        TestDataFactory.reset();

        List<com.team10.domain.Team> teams = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            teams.add(TestDataFactory.createTeam(sport)); // BUG FIX: sport-aware
        }

        window.setSelectedSport(sport);
        window.getController().startNewGame(sport, teams);
        window.showLeague();
    }

    private void styleSportButton(Button btn, String bgColor, String borderColor) {
        String base = "-fx-background-color: " + bgColor + ";" +
                "-fx-border-color: " + borderColor + ";" +
                "-fx-border-width: 2; -fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: white; -fx-font-size: 18px;" +
                "-fx-font-weight: bold; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setPrefWidth(200); btn.setPrefHeight(55);
        btn.setFocusTraversable(false);
        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e  -> btn.setOpacity(1.0));
    }

    private void addHover(Button btn, Image img, Label text, String desc) {
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            if (img != null) { previewGif.setImage(img); previewGif.setVisible(true); }
            text.setText(desc);
        });
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            previewGif.setVisible(false);
            text.setText("Hover over a sport to preview");
        });
    }

    private Image loadImage(String path) {
        try {
            var res = getClass().getResourceAsStream(path);
            if (res == null) return null;
            return new Image(res);
        } catch (Exception e) { return null; }
    }

    public Parent getRoot() { return root; }
}
