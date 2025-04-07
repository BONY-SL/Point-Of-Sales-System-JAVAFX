package org.pos.project.possystem.util;

import javafx.scene.control.Alert;

public final class CustomAlert {

    private CustomAlert() {
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
}
