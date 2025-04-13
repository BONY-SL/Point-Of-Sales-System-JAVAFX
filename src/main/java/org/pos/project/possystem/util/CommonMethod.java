package org.pos.project.possystem.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class CommonMethod {


    private static final String ADMIN_DASHBOARD_FXML = "/org/pos/project/possystem/admin-dashboard.fxml";

    private CommonMethod() {
    }

    public static void showAlert(String title, String message, CustomAlertType customAlertType) {
        Alert.AlertType alertType = switch (customAlertType) {
            case WARNING -> Alert.AlertType.WARNING;
            case INFORMATION -> Alert.AlertType.INFORMATION;
            case CONFIRMATION -> Alert.AlertType.CONFIRMATION;
            case ERROR -> Alert.AlertType.ERROR;
        };

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void goToBack(Event event, Class<?> getClass) {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass.getResource(ADMIN_DASHBOARD_FXML)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
