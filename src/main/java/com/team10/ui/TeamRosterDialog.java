package com.team10.ui;

import com.team10.domain.Player;
import com.team10.domain.Team;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

/**
 * YENİ: Takım ismine tıklandığında açılan oyuncu listesi penceresi.
 * Oyuncunun adı, pozisyon, skill ve sakatlık durumunu gösterir.
 */
public class TeamRosterDialog {

    public static void show(MainWindow window, Team team) {
        Stage dialog = new Stage();
        dialog.initOwner(window.getStage());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle(team.getName() + " - Squad");

        Label title = new Label("🏟  " + team.getName());
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        String coachInfo = team.getCoach() != null
                ? "👔  Coach: " + team.getCoach().getName() + " (Bonus +" + team.getCoach().getTrainingBonus() + ")"
                : "No coach";
        Label coachLbl = new Label(coachInfo);
        coachLbl.setStyle("-fx-text-fill: #90caf9; -fx-font-size: 13px;");

        // Oyuncu tablosu
        TableView<Player> table = new TableView<>();
        table.setFixedCellSize(30);
        table.setStyle("-fx-background-color:rgba(0,0,0,0.6); -fx-control-inner-background:rgba(20,40,60,0.7);");

        TableColumn<Player, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));
        nameCol.setPrefWidth(160);

        TableColumn<Player, String> posCol = new TableColumn<>("Position");
        posCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPosition()));
        posCol.setPrefWidth(100);

        TableColumn<Player, Integer> skillCol = new TableColumn<>("Skill");
        skillCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(d.getValue().getSkill()).asObject());
        skillCol.setPrefWidth(60);

        TableColumn<Player, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> {
            Player p = d.getValue();
            String s = p.isInjured() ? "🤕 INJ (" + p.getInjuryMatches() + " left)" : "✅ OK";
            return new javafx.beans.property.SimpleStringProperty(s);
        });
        statusCol.setPrefWidth(120);

        table.getColumns().addAll(nameCol, posCol, skillCol, statusCol);
        table.getItems().addAll(team.getPlayers());

        // Satır rengi: sakatlı kırmızı
        table.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null && item.isInjured())
                    setStyle("-fx-background-color: rgba(220,50,50,0.25);");
                else
                    setStyle("");
            }
        });

        int rows = team.getPlayers().size();
        table.setPrefHeight(35 + rows * 30.0);

        Label injured = new Label();
        long injCount = team.getPlayers().stream().filter(Player::isInjured).count();
        if (injCount > 0) {
            injured.setText("⚠  " + injCount + " player(s) injured");
            injured.setStyle("-fx-text-fill: #ff7043; -fx-font-size: 13px;");
        }

        Button close = new Button("Close");
        UIHelper.style(close);
        close.setOnAction(e -> dialog.close());

        VBox box = new VBox(12, title, coachLbl, table, injured, close);
        box.setAlignment(Pos.TOP_LEFT);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color:rgba(12,22,36,0.97);" +
                "-fx-border-color:rgba(79,195,247,0.5);" +
                "-fx-border-width:1.5;-fx-background-radius:14;-fx-border-radius:14;");

        Scene sc = new Scene(box, 480, Math.min(600, 160 + rows * 30));
        sc.setFill(Color.TRANSPARENT);
        dialog.setScene(sc);
        dialog.showAndWait();
    }
}
