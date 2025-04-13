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

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(txtId.getText());
        supplierDTO.setName(txtName.getText());
        supplierDTO.setAddress(txtAddress.getText());
        supplierDTO.setContact(txtTel.getText());

        int result = SupplierModel.saveSupplier(supplierDTO);
        if(result >= 0){
            logger.info("New Supplier Record Added Successfully");
            initialize();
        }else {
            logger.info("New Supplier Record Not Added Successfully");
        }

    }

    @FXML
    void deleteBtn(ActionEvent event) {

        int result = SupplierModel.deleteSupplier(txtId.getText());

        if(result > 0){
            logger.info("Supplier Record Deleted Successfully");
            initialize();
        }else {
            logger.info("Supplier Record Deleted not Successfully");
        }

    }

    @FXML
    void searchBtn(ActionEvent event) {

        SupplierDTO supplierDTO = SupplierModel.searchSupplier(txtId.getText());

        if (supplierDTO != null){
            txtId.setText(supplierDTO.getId());
            txtName.setText(supplierDTO.getName());
            txtAddress.setText(supplierDTO.getAddress());
            txtTel.setText(supplierDTO.getContact());
        }else {
            logger.info("Invalid Supplier ID");
        }
    }

    @FXML
    void updateBtn(ActionEvent event) {

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(txtId.getText());
        supplierDTO.setName(txtName.getText());
        supplierDTO.setAddress(txtAddress.getText());
        supplierDTO.setContact(txtTel.getText());

        int result = SupplierModel.updateSupplier(supplierDTO);

        if(result > 0){
            logger.info("Supplier Record Update Successfully");
            initialize();
        }else {
            logger.info("Supplier Record Not Update Successfully");
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
