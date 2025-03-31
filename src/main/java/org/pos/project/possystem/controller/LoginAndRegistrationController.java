package org.pos.project.possystem.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;

public class LoginAndRegistrationController {

    @FXML
    private AnchorPane loginPane;

    @FXML
    private AnchorPane registrationPane;

    @FXML
    private Button goToRegistrationButton;

    @FXML
    private Button backToLoginButton;

    @FXML
    public void initialize() {
        // Action for "Go To Registration" button
        goToRegistrationButton.setOnAction(event -> {
            loginPane.setVisible(false);         // Hide the login form
            registrationPane.setVisible(true);   // Show the registration form
        });

        // Action for "Back to Login" button
        backToLoginButton.setOnAction(event -> {
            registrationPane.setVisible(false);  // Hide the registration form
            loginPane.setVisible(true);          // Show the login form
        });
    }
}
