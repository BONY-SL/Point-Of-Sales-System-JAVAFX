package org.pos.project.possystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProductDetailsController {

    @FXML
    private TableColumn<?, ?> s_address;

    @FXML
    private TableColumn<?, ?> s_id;

    @FXML
    private TableColumn<?, ?> s_name;

    @FXML
    private TableColumn<?, ?> s_tell;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    public void initialize(){

    }

    @FXML
    void addBtn(ActionEvent event) {

    }

    @FXML
    void deleteBtn(ActionEvent event) {

    }

    @FXML
    void searchBtn(ActionEvent event) {

    }

    @FXML
    void updateBtn(ActionEvent event) {

    }

}
