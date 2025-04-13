package org.pos.project.possystem.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.pos.project.possystem.dto.UserDTO;
import org.pos.project.possystem.model.UserModel;
import org.pos.project.possystem.tm.User;
import org.pos.project.possystem.util.CommonMethod;

import java.util.ArrayList;
import java.util.List;

public class ManageUserController {

    @FXML
    private TableColumn<?, ?> email;

    @FXML
    private TableColumn<?, ?> firstName;

    @FXML
    private TableColumn<?, ?> lastName;

    @FXML
    private TableColumn<?, ?> role;

    @FXML
    private TableColumn<?, ?> userId;

    @FXML
    private TableView<User> userTable;

    @FXML
    public void initialize() {

        userTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        userTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("firstName"));
        userTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("lastName"));
        userTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("email"));
        userTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("userType"));

        ArrayList<User> userArrayList = (ArrayList<User>) getAllUsers();
        userTable.setItems(FXCollections.observableArrayList(userArrayList));

    }

    @FXML
    void deleteUser(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {
        CommonMethod.goToBack(event,getClass());
    }

    private List<User> getAllUsers(){

        ArrayList<User> userArrayList = new ArrayList<>();

        ArrayList<UserDTO> userDTOArrayList = (ArrayList<UserDTO>) UserModel.getAllUsers();

        for (var userDto : userDTOArrayList){
            var product = User.builder()
                    .id(userDto.getId())
                    .email(userDto.getEmail())
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .userType(userDto.getUserType())
                    .build();
            userArrayList.add(product);
        }

        return userArrayList;
    }

}
