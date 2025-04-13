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
import org.pos.project.possystem.util.CommonMethod;
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
        if (productId.getValue() == null || quantity.getText().isEmpty()) {
            CommonMethod.showAlert("Validation Error", "Please select a product and enter a quantity.", CustomAlertType.WARNING);
            return;
        }

        try {
            double qty = Double.parseDouble(quantity.getText());

            if (qty <= 0) {
                CommonMethod.showAlert("Invalid Quantity", "Quantity must be greater than zero.", CustomAlertType.WARNING);
                return;
            }

            var newStock = ProductStockDTO.builder()
                    .productId(productId.getValue())
                    .quantity(qty)
                    .build();

            int result = ProductStockModel.saveNewStock(newStock);

            if(result >= 0){
                logger.info("New Stock Record Added Successfully");
                CommonMethod.showAlert("INFO", "New Stock Record Added Successfully", CustomAlertType.INFORMATION);
                initialize();
            } else {
                logger.info("New Stock Record Not Added Successfully");
                CommonMethod.showAlert("ERROR", "New Stock Record Not Added Successfully", CustomAlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            CommonMethod.showAlert("Invalid Input", "Please enter a valid number for Quantity.", CustomAlertType.WARNING);
        }
    }


    @FXML
    void deleteBtn(ActionEvent event) {

        if (selectedProductStock == null){
            logger.info("Select The Row First....");
            CommonMethod.showAlert("INFO","Select The Row First....",CustomAlertType.WARNING);

        }else {
            stockId.setText(String.valueOf(selectedProductStock.getId()));
            int result = ProductStockModel.deleteProductStock(selectedProductStock.getId());
            if(result > 0){
                logger.info("Stock Record Deleted Successfully");
                CommonMethod.showAlert("INFO","Stock Record Deleted Successfully",CustomAlertType.INFORMATION);

                initialize();
            }else {
                logger.info("Stock Record Deleted not Successfully");
                CommonMethod.showAlert("ERROR","Stock Record Deleted not Successfully",CustomAlertType.ERROR);

            }
        }

    }

    @FXML
    void searchBtn(ActionEvent event) {
        if (productId.getValue() == null) {
            CommonMethod.showAlert("Validation Error", "Please select a Product ID.", CustomAlertType.WARNING);
            return;
        }

        ProductStockDTO productStockDTO = ProductStockModel.getStockDetails(productId.getValue());

        if (productStockDTO != null) {
            productId.setValue(productStockDTO.getProductId());
            quantity.setText(String.valueOf(productStockDTO.getQuantity()));
            stockId.setText(String.valueOf(productStockDTO.getId()));
            updateTime.setText(String.valueOf(productStockDTO.getUpdateTime()));
        } else {
            CommonMethod.showAlert("Out Of Stock", "Stock is not available for the selected product.", CustomAlertType.WARNING);
            quantity.clear();
            stockId.clear();
            updateTime.setText("");
        }
    }


    @FXML
    void updateBtn(ActionEvent event) {

        if (productId.getValue() == null || quantity.getText().isEmpty()) {
            CommonMethod.showAlert("Validation Error", "Please select a product and make sure stock ID and quantity are filled.", CustomAlertType.WARNING);
            return;
        }

        if (Double.parseDouble(quantity.getText()) <= 0) {
            CommonMethod.showAlert("Invalid Quantity", "Quantity must be greater than zero.", CustomAlertType.WARNING);
            return;
        }


        var newStock = ProductStockDTO.builder()
                .productId(productId.getValue())
                .quantity(Double.parseDouble(quantity.getText())).build();

        int result = ProductStockModel.saveNewStock(newStock);

        if(result >= 0){
            logger.info("New Stock Record Update Successfully");
            CommonMethod.showAlert("INFO","New Stock Record Update Successfully",CustomAlertType.INFORMATION);
            initialize();
        }else {
            logger.info("New Stock Record Not Update Successfully");
            CommonMethod.showAlert("ERROR","New Stock Record Not Update Successfully",CustomAlertType.ERROR);

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

    @FXML
    void goBack(ActionEvent event) {

        CommonMethod.goToBack(event,getClass());

    }
}

