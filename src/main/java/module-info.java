module org.pos.project.possystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.pos.project.possystem to javafx.fxml;
    exports org.pos.project.possystem;
}