module sports.manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.logging;

    // UI package needs access to JavaFX
    opens com.team10.ui to javafx.graphics, javafx.fxml;

    // Domain classes need to be open for Java Serialization (ObjectOutputStream)
    opens com.team10.domain to java.base;
    opens com.team10.sports to java.base;
    opens com.team10.persistence to java.base;

    exports com.team10;
    exports com.team10.ui;
    exports com.team10.domain;
    exports com.team10.sports;
    exports com.team10.persistence;
}
