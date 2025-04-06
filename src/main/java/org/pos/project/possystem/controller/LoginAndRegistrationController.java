package org.pos.project.possystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.pos.project.possystem.exception.UserEmailExsist;
import org.pos.project.possystem.exception.UserNotFound;
import org.pos.project.possystem.model.UserModel;
import org.pos.project.possystem.tm.Admin;
import org.pos.project.possystem.tm.Cashier;
import org.pos.project.possystem.tm.User;
import org.pos.project.possystem.util.CustomAlert;
import org.pos.project.possystem.util.CustomAlertType;
import org.pos.project.possystem.util.UserType;

public class LoginAndRegistrationController {

    @FXML
    private Button backToLoginButton;

    @FXML
    private Button goToRegistrationButton;

    @FXML
    private AnchorPane imagePane;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField regFirstNameField1;

    @FXML
    private TextField regLastNameField11;

    @FXML
    private PasswordField regPassword;

    @FXML
    private PasswordField regReEnterPassword;

    @FXML
    private TextField regUsernameField;

    @FXML
    private AnchorPane registrationPane;

    @FXML
    private ComboBox<UserType> roleBox;

    @FXML
    private TextField usernameField;

    @FXML
    public void initialize() {

        ObservableList<UserType> observableList = FXCollections.observableArrayList(UserType.values());
        roleBox.setItems(observableList);

        goToRegistrationButton.setOnAction(event -> {
            loginPane.setVisible(false);
            registrationPane.setVisible(true);
        });

        backToLoginButton.setOnAction(event -> {
            registrationPane.setVisible(false);
            loginPane.setVisible(true);
        });


    }

    @FXML
    void login(ActionEvent event) {

        String getUsername = usernameField.getText();
        String password = passwordField.getText();

        if (getUsername.isEmpty() || password.isEmpty()) {
            CustomAlert.showAlert("Login Error", "Please Enter User Name and Password", CustomAlertType.ERROR);
            return;
        }
        if (!getUsername.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            CustomAlert.showAlert("Invalid Email", "Please enter a valid email address.", CustomAlertType.WARNING);
            return;
        }
        try {
            User user = User.builder()
                    .email(getUsername).password(password).build();
            UserModel.login(user);
        }catch (UserNotFound e){
            CustomAlert.showAlert("Error", e.getMessage(), CustomAlertType.WARNING);
        }

    }

    @FXML
    void register(ActionEvent event) {


        String email = regUsernameField.getText();
        String password = regPassword.getText();
        String reEnterPassword = regReEnterPassword.getText();
        String firstName = regFirstNameField1.getText();
        String lastName = regLastNameField11.getText();
        UserType userType = roleBox.getValue();


        if (email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty() ||
                firstName.isEmpty() || lastName.isEmpty() || userType == null) {
            CustomAlert.showAlert("Form Error", "Please fill all fields.", CustomAlertType.WARNING);
            return;
        }
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            CustomAlert.showAlert("Invalid Email", "Please enter a valid email address.", CustomAlertType.ERROR);
            return;
        }
        if (!password.equals(reEnterPassword)) {
            CustomAlert.showAlert("Password Error", "Passwords do not match.", CustomAlertType.ERROR);
            return;
        }

        User user = null;

        if(userType.equals(UserType.ADMIN)){

             user = Admin.builder()
                    .email(email)
                    .password(password)
                    .firstName(firstName)
                    .lastName(lastName)
                    .userType(UserType.ADMIN)
                    .build();

        } else if (userType.equals(UserType.CASHIER)) {

             user = Cashier.builder()
                    .email(email)
                    .password(password)
                    .firstName(firstName)
                    .lastName(lastName)
                    .userType(UserType.CASHIER)
                    .build();
        }
        System.out.println(user);
        try {
            UserModel.registerUser(user);
            CustomAlert.showAlert("Success", "Registration Successful!", CustomAlertType.INFORMATION);
            clearFields();
        } catch (UserEmailExsist e) {
            CustomAlert.showAlert("Email Error", e.getMessage(), CustomAlertType.ERROR);
        }

    }

    private void clearFields() {
        regUsernameField.clear();
        regPassword.clear();
        regReEnterPassword.clear();
        regFirstNameField1.clear();
        regLastNameField11.clear();
        roleBox.setValue(null);
    }

    @FXML
    void showHidePassword(ActionEvent event) {

    }


}
