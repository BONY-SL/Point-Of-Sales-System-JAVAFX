module org.pos.project.possystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires static lombok;



    opens org.pos.project.possystem to javafx.fxml;
    exports org.pos.project.possystem;
    exports org.pos.project.possystem.tm;
    opens org.pos.project.possystem.tm to javafx.fxml;
    exports org.pos.project.possystem.controller;
    opens org.pos.project.possystem.controller to javafx.fxml;
}