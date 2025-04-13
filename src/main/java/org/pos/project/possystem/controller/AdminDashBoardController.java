package org.pos.project.possystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.pos.project.possystem.dto.UserDTO;
import org.pos.project.possystem.model.UserModel;
import org.pos.project.possystem.tm.User;
import org.pos.project.possystem.util.CommonMethod;
import org.pos.project.possystem.util.CustomAlertType;
import org.pos.project.possystem.util.SessionManager;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class AdminDashBoardController {


    @FXML
    private BorderPane dashboardPane;

    @FXML
    private PasswordField password;

    @FXML
    private TextField role;

    @FXML
    private TextField userEmail;

    @FXML
    private TextField userName;

    @FXML
    public void initialize(){

            User user = SessionManager.getCurrentUser();
            if (user != null) {
                userEmail.setText(user.getEmail());
                userName.setText(user.getFirstName() + " " + user.getLastName());
                role.setText(user.getUserType().name());
                password.setText(user.getPassword());
            }
    }

    public void setLoggedInUser(User user) {
        userEmail.setText(user.getEmail());
        userName.setText(user.getFirstName() + " " + user.getLastName());
        role.setText(user.getUserType().name());
        password.setText(user.getPassword());
    }

    @FXML
    void product(ActionEvent event) {

        loadFullPage("products-view");

    }

    @FXML
    void report(ActionEvent event) {

        loadFullPage("report-view");

    }

    @FXML
    void stock(ActionEvent event) {

        loadFullPage("stock-details");

    }

    @FXML
    void supplier(ActionEvent event) {

        loadFullPage("supplier-view");

    }

    @FXML
    void transaction(ActionEvent event) {

        loadFullPage("place-order");

    }

    @FXML
    void userManage(ActionEvent event) {

        loadFullPage("manage-user");

    }

    private void loadFullPage(String page) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/pos/project/possystem/" + page + ".fxml")));
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(capitalize(page));
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load page: " + page);
            e.printStackTrace();
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).replace("-", " ") + " Page";
    }



    @FXML
    void logOut(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setContentText("Are you sure you want to log out?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/pos/project/possystem/login-view.fxml")));
                Scene scene = new Scene(root);

                Stage stage = (Stage) dashboardPane.getScene().getWindow();

                stage.setScene(scene);
                stage.setTitle("Login");
                stage.show();
                SessionManager.clearSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private CheckBox checkBoxId;

    @FXML
    private TextField plainPassword;

    @FXML
    void showHidePassword(ActionEvent event) {
        if (checkBoxId.isSelected()) {
            plainPassword.setText(password.getText());
            plainPassword.setVisible(true);
            password.setVisible(false);
        } else {

            password.setText(plainPassword.getText());
            password.setVisible(true);
            plainPassword.setVisible(false);
        }
    }

    @FXML
    private Button updateButton;

    @FXML
    void updateUserProfile(ActionEvent event) {
        // Toggle button state and field visibility
        if (updateButton.getText().equals("Save")) {

            String getEmail = userEmail.getText();
            String getPassword = plainPassword.getText();

            if (getEmail.isEmpty() || getPassword.isEmpty()) {
                CommonMethod.showAlert("WARNING", "Please Enter Email and Password", CustomAlertType.WARNING);
                return;
            }
            if (!getEmail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                CommonMethod.showAlert("Invalid Email", "Please enter a valid email address.", CustomAlertType.WARNING);
                return;
            }

            var userDto = UserDTO.builder()
                    .email(getEmail)
                    .password(getPassword)
                    .build();

            int result = UserModel.updateUserDetails(userDto);

            if (result == -1) {
                CommonMethod.showAlert("ERROR", "This email is already used by another account.", CustomAlertType.ERROR);
                return;
            }

            if (result > 0) {
                CommonMethod.showAlert("SUCCESS", "Profile updated successfully.", CustomAlertType.INFORMATION);
            } else {
                CommonMethod.showAlert("ERROR", "Failed to update profile.", CustomAlertType.ERROR);
            }

            password.setEditable(false);
            userEmail.setEditable(false);
            checkBoxId.setDisable(true);
            password.setText(plainPassword.getText());
            password.setVisible(true);
            plainPassword.setVisible(false);
            updateButton.setText("Update");
            updateButton.setStyle("-fx-background-color: #4d0c80; -fx-text-fill: white;");
        } else {
            updateButton.setText("Save");
            plainPassword.setEditable(true);
            userEmail.setEditable(true);
            checkBoxId.setDisable(false);
            plainPassword.setText(password.getText());
            plainPassword.setVisible(true);
            password.setVisible(false);
            updateButton.setStyle("-fx-background-color: #b72a2a; -fx-text-fill: white;");
        }
    }

}
