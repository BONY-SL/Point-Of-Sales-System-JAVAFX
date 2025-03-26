module org.pos.project.possystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens org.pos.project.possystem to javafx.fxml;
    exports org.pos.project.possystem;
}