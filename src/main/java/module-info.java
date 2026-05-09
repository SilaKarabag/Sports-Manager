module sports.manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.team10.ui to javafx.graphics, javafx.fxml, javafx.media;

    exports com.team10;
    exports com.team10.ui;
}