package org.pos.project.possystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.Objects;

public class LoginAndRegistrationController {

    @FXML
    private Button backToLoginButton;

    @FXML
    private Button goToRegistrationButton;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField regPasswordField;

    @FXML
    private TextField regUsernameField;

    @FXML
    private TextField regUsernameField1;

    @FXML
    private TextField regUsernameField11;

    @FXML
    private TextField regUsernameField111;

    @FXML
    private AnchorPane registrationPane;

    @FXML
    private TextField usernameField;

    @FXML
    private AnchorPane imagePane;


    @FXML
    private ImageView imageView;

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
