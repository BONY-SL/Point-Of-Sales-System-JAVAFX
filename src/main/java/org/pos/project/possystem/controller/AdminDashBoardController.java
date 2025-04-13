package org.pos.project.possystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.pos.project.possystem.tm.User;
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

        loadFullPage("");

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
    void updateUserProfile(ActionEvent event) {

    }

}
