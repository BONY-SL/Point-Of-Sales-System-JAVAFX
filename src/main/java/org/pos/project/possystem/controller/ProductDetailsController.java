package org.pos.project.possystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.pos.project.possystem.model.ProductModel;

import java.util.ArrayList;

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
    private TextField txtDes;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private ComboBox<String> txtid;

    @FXML
    public void initialize() {

        ArrayList<String> stringArrayList = (ArrayList<String>) ProductModel.getSuppliersId();

        ObservableList<String> observableList = FXCollections.observableArrayList(stringArrayList);
        txtid.setItems(observableList);
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
