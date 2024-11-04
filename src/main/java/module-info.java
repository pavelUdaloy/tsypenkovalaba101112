module org.example.tsypenkovalaba101112 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens org.example.tsypenkovalaba101112 to javafx.fxml;
    exports org.example.tsypenkovalaba101112;
    exports org.example.tsypenkovalaba101112.db;
    opens org.example.tsypenkovalaba101112.db to javafx.fxml;
    exports org.example.tsypenkovalaba101112.entity;
    opens org.example.tsypenkovalaba101112.entity to javafx.fxml;
}