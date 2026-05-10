package com.team10.ui;

import com.team10.domain.TestDataFactory;
import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SportSelectionView {

    private final BorderPane root;
    private final ImageView previewImg = new ImageView();

    public SportSelectionView(MainWindow window) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e, #0f3460);");

        Label title = new Label("SELECT YOUR SPORT");
        title.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: white;" +
                "-fx-effect: dropshadow(gaussian,#4fc3f7,20,0.5,0,0);");

        Button football   = sportButton("⚽  Football",   "#0d47a1", "#42a5f5");
        Button volleyball = sportButton("🏐  Volleyball", "#4a148c", "#ce93d8");

        Label infoLabel = new Label("4 teams · Double round-robin league · Save & Load supported");
        infoLabel.setStyle("-fx-text-fill: #90caf9; -fx-font-size: 13px;");

        // Preview
        previewImg.setFitWidth(360); previewImg.setFitHeight(200);
        previewImg.setPreserveRatio(false);
        previewImg.setVisible(false);
        previewImg.setClip(new Rectangle(360, 200) {{ setArcWidth(20); setArcHeight(20); }});

        Rectangle previewBox = new Rectangle(360, 200);
        previewBox.setArcWidth(20); previewBox.setArcHeight(20);
        previewBox.setFill(Color.rgb(255,255,255,0.06));
        previewBox.setStroke(Color.rgb(255,255,255,0.15));

        Label previewText = new Label("Hover to preview");
        previewText.setStyle("-fx-text-fill: #90caf9; -fx-font-size: 14px;");

        Image fbImg  = loadImage("/images/football.gif");
        Image vbImg  = loadImage("/images/volleyball.jpg");

        addHover(football,   fbImg,  previewText, "Football  ·  11 players  ·  2 Halves  ·  3pts win");
        addHover(volleyball, vbImg,  previewText, "Volleyball  ·  6 players  ·  Best of 5 Sets  ·  3pts win");

        football.setOnAction(e   -> startGame(window, new FootballSport()));
        volleyball.setOnAction(e -> startGame(window, new VolleyballSport()));

        HBox btns = new HBox(40, football, volleyball);
        btns.setAlignment(Pos.CENTER);

        VBox center = new VBox(28, title, btns, new StackPane(previewBox, previewImg), previewText, infoLabel);
        center.setAlignment(Pos.CENTER);

        Button menuBtn = MenuOverlay.createMenuButton(window);
        HBox top = new HBox(menuBtn);
        top.setAlignment(Pos.TOP_RIGHT);
        top.setPadding(new Insets(16));

        root.setTop(top);
        root.setCenter(center);
    }

    private void startGame(MainWindow window, Sport sport) {
        // FIX: reset sayaçlar, sport-aware takımlar oluştur
        TestDataFactory.reset();
        List<com.team10.domain.Team> teams = new ArrayList<>();
        for (int i = 0; i < 4; i++) teams.add(TestDataFactory.createTeam(sport));
        window.getController().startNewGame(sport, teams);
        window.showLeague();
    }

    private Button sportButton(String text, String bg, String border) {
        Button b = new Button(text);
        String s = "-fx-background-color:" + bg + "; -fx-border-color:" + border + ";" +
                "-fx-border-width:2; -fx-border-radius:12; -fx-background-radius:12;" +
                "-fx-text-fill:white; -fx-font-size:18px; -fx-font-weight:bold; -fx-cursor:hand;";
        b.setStyle(s);
        b.setPrefWidth(210); b.setPrefHeight(58);
        b.setFocusTraversable(false);
        b.setOnMouseEntered(e -> b.setOpacity(0.82));
        b.setOnMouseExited(e  -> b.setOpacity(1.0));
        return b;
    }

    private void addHover(Button btn, Image img, Label txt, String desc) {
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            if (img != null) { previewImg.setImage(img); previewImg.setVisible(true); }
            txt.setText(desc);
        });
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            previewImg.setVisible(false);
            txt.setText("Hover to preview");
        });
    }

    private Image loadImage(String path) {
        try { var r = getClass().getResourceAsStream(path); return r != null ? new Image(r) : null; }
        catch (Exception e) { return null; }
    }

    public Parent getRoot() { return root; }
}
