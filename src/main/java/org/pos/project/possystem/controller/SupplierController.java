package org.pos.project.possystem.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.pos.project.possystem.dto.SupplierDTO;
import org.pos.project.possystem.model.SupplierModel;
import org.pos.project.possystem.tm.Supplier;
import org.pos.project.possystem.util.CommonMethod;
import org.pos.project.possystem.util.CustomAlertType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class SupplierController {

    @FXML
    private TableView<Supplier> tableView;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    private final Logger logger = Logger.getLogger(SupplierController.class.getName());

    @FXML
    public void initialize(){

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));

        ArrayList<Supplier> supplierArrayList = (ArrayList<Supplier>) getAllSuppliers();
        tableView.setItems(FXCollections.observableArrayList(supplierArrayList));

    }

    @FXML
    void addBtn(ActionEvent event) {
        // Input validation
        if (txtId.getText().isEmpty() || txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtTel.getText().isEmpty()) {
            CommonMethod.showAlert("Validation Error", "Please fill in all the fields.", CustomAlertType.WARNING);
            return;
        }

        if (!txtTel.getText().matches("\\d{10}")) {
            CommonMethod.showAlert("Invalid Contact", "Please enter a valid 10-digit contact number.", CustomAlertType.WARNING);
            return;
        }

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(txtId.getText());
        supplierDTO.setName(txtName.getText());
        supplierDTO.setAddress(txtAddress.getText());
        supplierDTO.setContact(txtTel.getText());

        int result = SupplierModel.saveSupplier(supplierDTO);
        if (result >= 0) {
            logger.info("New Supplier Record Added Successfully");
            CommonMethod.showAlert("INFO", "New Supplier Record Added Successfully", CustomAlertType.INFORMATION);
            initialize();
        } else {
            logger.info("New Supplier Record Not Added Successfully");
            CommonMethod.showAlert("ERROR", "New Supplier Record Not Added Successfully", CustomAlertType.ERROR);
        }
    }


    @FXML
    void deleteBtn(ActionEvent event) {
        String supplierId = txtId.getText();

        // Check if ID is entered
        if (supplierId.isEmpty()) {
            CommonMethod.showAlert("Validation Error", "Please enter or select a Supplier ID to delete.", CustomAlertType.WARNING);
            return;
        }

        // Optional confirmation dialog
        boolean confirmed = CommonMethod.showConfirmation("Confirm Deletion", "Are you sure you want to delete this supplier?");
        if (!confirmed) return;

        int result = SupplierModel.deleteSupplier(supplierId);

        if (result > 0) {
            logger.info("Supplier Record Deleted Successfully");
            CommonMethod.showAlert("INFO", "Supplier Record Deleted Successfully", CustomAlertType.INFORMATION);
            initialize();
        } else {
            logger.info("Supplier Record Not Deleted Successfully");
            CommonMethod.showAlert("ERROR", "Supplier Record Not Deleted Successfully", CustomAlertType.ERROR);
        }
    }


    @FXML
    void searchBtn(ActionEvent event) {

        String supplierId = txtId.getText();

        if (supplierId.isEmpty()) {
            CommonMethod.showAlert("Validation Error", "Please enter a Supplier ID.", CustomAlertType.WARNING);
            return;
        }

        SupplierDTO supplierDTO = SupplierModel.searchSupplier(supplierId);

        if (supplierDTO != null) {
            txtId.setText(supplierDTO.getId());
            txtName.setText(supplierDTO.getName());
            txtAddress.setText(supplierDTO.getAddress());
            txtTel.setText(supplierDTO.getContact());
        } else {
            logger.info("Invalid Supplier ID");
            CommonMethod.showAlert("WARNING", "Invalid Supplier ID", CustomAlertType.WARNING);

            txtName.clear();
            txtAddress.clear();
            txtTel.clear();
        }
    }


    @FXML
    void updateBtn(ActionEvent event) {
        // Validate required fields
        if (txtId.getText().isEmpty() || txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtTel.getText().isEmpty()) {
            CommonMethod.showAlert("Validation Error", "Please fill in all the fields before updating.", CustomAlertType.WARNING);
            return;
        }

        // Optional: validate contact number format
        if (!txtTel.getText().matches("\\d{10}")) {
            CommonMethod.showAlert("Invalid Contact", "Please enter a valid 10-digit contact number.", CustomAlertType.WARNING);
            return;
        }

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(txtId.getText());
        supplierDTO.setName(txtName.getText());
        supplierDTO.setAddress(txtAddress.getText());
        supplierDTO.setContact(txtTel.getText());

        int result = SupplierModel.updateSupplier(supplierDTO);

        if (result > 0) {
            logger.info("Supplier Record Updated Successfully");
            CommonMethod.showAlert("INFO", "Supplier Record Updated Successfully", CustomAlertType.INFORMATION);
            initialize();
        } else {
            logger.info("Supplier Record Not Updated Successfully");
            CommonMethod.showAlert("ERROR", "Supplier Record Not Updated Successfully", CustomAlertType.ERROR);
        }
    }


    private List<Supplier> getAllSuppliers(){

        ArrayList<Supplier> supplierArrayList = new ArrayList<>();

        ArrayList<SupplierDTO> supplierDTOArrayList = (ArrayList<SupplierDTO>) SupplierModel.getAllSuppliers();

        for (SupplierDTO supplierDTO : supplierDTOArrayList){
            Supplier supplier = new Supplier();
            supplier.setId(supplierDTO.getId());
            supplier.setName(supplierDTO.getName());
            supplier.setAddress(supplierDTO.getAddress());
            supplier.setContact(supplierDTO.getContact());
            supplierArrayList.add(supplier);
        }
        return supplierArrayList;

    }

    @FXML
    void goBack(ActionEvent event) {

        CommonMethod.goToBack(event,getClass());

    }
}
