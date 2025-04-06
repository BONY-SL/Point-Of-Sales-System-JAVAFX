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

        var newProduct = ProductDTO.builder()
                .name(txtName.getText())
                .description(txtDes.getText())
                .unitPrice(Double.parseDouble(txtPrice.getText()))
                .supplierId(txtid.getValue()).build();

        int result = ProductModel.saveNewProduct(newProduct);

        if(result >= 0){
            logger.info("New Product Record Added Successfully");
            initialize();
        }else {
            logger.info("New Product Record Not Added Successfully");
        }
    }

    @FXML
    void deleteBtn(ActionEvent event) {


        if (selectedProduct == null){
            logger.info("Select The Row First....");
        }else {
            int result = ProductModel.deleteProduct(selectedProduct.getId());
            if(result > 0){
                logger.info("Product Record Deleted Successfully");
                initialize();
            }else {
                logger.info("Product Record Deleted not Successfully");
            }
        }
    }

    @FXML
    void searchBtn(ActionEvent event) {

        txtName.setText(selectedProduct.getName());
        txtDes.setText(selectedProduct.getDescription());
        txtPrice.setText(String.valueOf(selectedProduct.getUnitPrice()));
        txtid.setValue(selectedProduct.getSupplierId());

    }

    @FXML
    private void updateBtn(ActionEvent event) {

        if (selectedProduct == null) {
            logger.info("Select The Row First....");
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
                initialize();
            } else {
                logger.info("Product Record Not Updated Successfully");
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

}
