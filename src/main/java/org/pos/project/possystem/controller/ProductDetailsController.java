package org.pos.project.possystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.pos.project.possystem.dto.ProductDTO;
import org.pos.project.possystem.model.ProductModel;
import org.pos.project.possystem.tm.Product;
import org.pos.project.possystem.util.CommonMethod;
import org.pos.project.possystem.util.CustomAlertType;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

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
    private TableView<Product> tableView;

    @FXML
    private TextField txtDes;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private ComboBox<String> txtid;

    @FXML
    private Button updateButton;

    private final Logger logger = Logger.getLogger(ProductDetailsController.class.getName());

    private Product selectedProduct;

    @FXML
    public void initialize() {

        ArrayList<String> stringArrayList = (ArrayList<String>) ProductModel.getSuppliersId();

        ObservableList<String> observableList = FXCollections.observableArrayList(stringArrayList);
        txtid.setItems(observableList);

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("description"));
        tableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        ArrayList<Product> supplierArrayList = (ArrayList<Product>) getAllProducts();
        tableView.setItems(FXCollections.observableArrayList(supplierArrayList));

        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, product, newValue) -> {
            selectedProduct = newValue;
        });
    }

    @FXML
    void addBtn(ActionEvent event) {

        if (txtName.getText().isEmpty() || txtDes.getText().isEmpty() || txtPrice.getText().isEmpty() || txtid.getValue() == null) {
            CommonMethod.showAlert("Validation Error", "Please fill in all fields.", CustomAlertType.WARNING);
            return;
        }

        try {
            double unitPrice = Double.parseDouble(txtPrice.getText());

            var newProduct = ProductDTO.builder()
                    .name(txtName.getText())
                    .description(txtDes.getText())
                    .unitPrice(unitPrice)
                    .supplierId(txtid.getValue())
                    .build();

            int result = ProductModel.saveNewProduct(newProduct);

            if(result >= 0){
                logger.info("New Product Record Added Successfully");
                CommonMethod.showAlert("SUCCESS", "New Product Record Added Successfully", CustomAlertType.INFORMATION);
                initialize();
            } else {
                logger.info("New Product Record Not Added Successfully");
                CommonMethod.showAlert("ERROR", "New Product Record Not Added Successfully", CustomAlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            CommonMethod.showAlert("Invalid Input", "Please enter a valid number for Unit Price.", CustomAlertType.WARNING);
        }
    }


    @FXML
    void deleteBtn(ActionEvent event) {


        if (selectedProduct == null){
            logger.info("Select The Row First....");
            CommonMethod.showAlert("INFO","Please Select Row First", CustomAlertType.WARNING);

        }else {
            int result = ProductModel.deleteProduct(selectedProduct.getId());
            if(result > 0){
                logger.info("Product Record Deleted Successfully");
                CommonMethod.showAlert("INFO","Product Record Deleted Successfully", CustomAlertType.INFORMATION);

                initialize();
            }else {
                logger.info("Product Record Deleted not Successfully");
                CommonMethod.showAlert("ERROR","Product Record Deleted not Successfully", CustomAlertType.ERROR);

            }
        }
    }

    @FXML
    void searchBtn(ActionEvent event) {

        if (selectedProduct == null){
            logger.info("Select The Row First....");
            CommonMethod.showAlert("INFO","Please Select Row First", CustomAlertType.WARNING);
            return;

        }

        txtName.setText(selectedProduct.getName());
        txtDes.setText(selectedProduct.getDescription());
        txtPrice.setText(String.valueOf(selectedProduct.getUnitPrice()));
        txtid.setValue(selectedProduct.getSupplierId());

    }

    @FXML
    private void updateBtn(ActionEvent event) {

        if (selectedProduct == null) {
            logger.info("Select The Row First....");
            CommonMethod.showAlert("INFO","Select The Row First....",CustomAlertType.WARNING);
            return;
        }

        if ("Save".equals(updateButton.getText())) {

            var product = ProductDTO.builder()
                    .id(selectedProduct.getId())
                    .name(txtName.getText())
                    .description(txtDes.getText())
                    .unitPrice(Double.parseDouble(txtPrice.getText()))
                    .supplierId(txtid.getValue()).build();

            int result = ProductModel.updateProductDetails(product);

            if (result > 0) {
                updateButton.setStyle("-fx-background-color: #7854ba; -fx-text-fill: white; -fx-font-weight: bold");
                updateButton.setText("Update");
                logger.info("Product Record Updated Successfully");
                CommonMethod.showAlert("INFO","Product Record Updated Successfully",CustomAlertType.INFORMATION);
                initialize();
            } else {
                logger.info("Product Record Not Updated Successfully");
                CommonMethod.showAlert("ERROR","Product Record Not Updated Successfully",CustomAlertType.ERROR);

            }
        } else {
            txtName.setText(selectedProduct.getName());
            txtDes.setText(selectedProduct.getDescription());
            txtPrice.setText(String.valueOf(selectedProduct.getUnitPrice()));
            txtid.setValue(selectedProduct.getSupplierId());

            updateButton.setStyle("-fx-background-color: #f83e3e; -fx-text-fill: white; -fx-font-weight: bold");
            updateButton.setText("Save");
        }
    }

    private List<Product> getAllProducts(){

        ArrayList<Product> productArrayList = new ArrayList<>();

        ArrayList<ProductDTO> productDTOArrayList = (ArrayList<ProductDTO>) ProductModel.getAllProducts();

        for (var productDTO : productDTOArrayList){
            var product = Product.builder()
                    .id(productDTO.getId())
                    .name(productDTO.getName())
                    .description(productDTO.getDescription())
                    .unitPrice(productDTO.getUnitPrice())
                    .supplierId(productDTO.getSupplierId())
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
