package org.pos.project.possystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.pos.project.possystem.dto.ProductStockDTO;
import org.pos.project.possystem.model.ProductStockModel;
import org.pos.project.possystem.tm.ProductStock;
import org.pos.project.possystem.util.CustomAlert;
import org.pos.project.possystem.util.CustomAlertType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StockController {

    @FXML
    private ComboBox<Integer> productId;

    @FXML
    private TextField quantity;

    @FXML
    private TableColumn<?, ?> s_address;

    @FXML
    private TableColumn<?, ?> s_id;

    @FXML
    private TableColumn<?, ?> s_name;

    @FXML
    private TableColumn<?, ?> s_tell;

    @FXML
    private TableView<ProductStock> tableView;

    @FXML
    private Button updateButton;

    @FXML
    private TextField stockId;

    @FXML
    private Label updateTime;

    private final Logger logger = Logger.getLogger(StockController.class.getName());

    private ProductStock selectedProductStock;

    @FXML
    void initialize(){

        ArrayList<Integer> getProductDetails = (ArrayList<Integer>) ProductStockModel.getAllProductId();
        ObservableList<Integer> observableList = FXCollections.observableArrayList(getProductDetails);
        productId.setItems(observableList);

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("productId"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("updateTime"));

        ArrayList<ProductStock> productArrayList = (ArrayList<ProductStock>) getAllProductStock();
        tableView.setItems(FXCollections.observableArrayList(productArrayList));

        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, productStock, newValue) -> {
            selectedProductStock = newValue;
        });
    }

    @FXML
    void addBtn(ActionEvent event) {

        var newStock = ProductStockDTO.builder()
                .productId(productId.getValue())
                .quantity(Double.parseDouble(quantity.getText())).build();

        int result = ProductStockModel.saveNewStock(newStock);

        if(result >= 0){
            logger.info("New Stock Record Added Successfully");
            initialize();
        }else {
            logger.info("New Stock Record Not Added Successfully");
        }
    }

    @FXML
    void deleteBtn(ActionEvent event) {

        if (selectedProductStock == null){
            logger.info("Select The Row First....");
        }else {
            stockId.setText(String.valueOf(selectedProductStock.getId()));
            int result = ProductStockModel.deleteProductStock(selectedProductStock.getId());
            if(result > 0){
                logger.info("Stock Record Deleted Successfully");
                initialize();
            }else {
                logger.info("Stock Record Deleted not Successfully");
            }
        }

    }

    @FXML
    void searchBtn(ActionEvent event) {

        ProductStockDTO productStockDTO = ProductStockModel.getStockDetails(productId.getValue());

        if(productStockDTO != null){
            productId.setValue(productStockDTO.getProductId());
            quantity.setText(String.valueOf(productStockDTO.getQuantity()));
            stockId.setText(String.valueOf(productStockDTO.getId()));
            updateTime.setText(String.valueOf(productStockDTO.getUpdateTime()));
        }else {
            CustomAlert.showAlert("Out Of Stock","Stoke is Not Available", CustomAlertType.WARNING);
        }

    }

    @FXML
    void updateBtn(ActionEvent event) {

        var newStock = ProductStockDTO.builder()
                .productId(productId.getValue())
                .quantity(Double.parseDouble(quantity.getText())).build();

        int result = ProductStockModel.saveNewStock(newStock);

        if(result >= 0){
            logger.info("New Stock Record Added Successfully");
            initialize();
        }else {
            logger.info("New Stock Record Not Added Successfully");
        }

    }

    private List<ProductStock> getAllProductStock(){

        ArrayList<ProductStock> productArrayList = new ArrayList<>();

        ArrayList<ProductStockDTO> productDTOArrayList = (ArrayList<ProductStockDTO>)
                ProductStockModel.getAllProductStock();

        for (var productDTO : productDTOArrayList){
            var product = ProductStock.builder()
                    .id(productDTO.getId())
                    .productId(productDTO.getProductId())
                    .quantity(productDTO.getQuantity())
                    .updateTime(productDTO.getUpdateTime())
                    .build();
            productArrayList.add(product);
        }

        return productArrayList;
    }
}

