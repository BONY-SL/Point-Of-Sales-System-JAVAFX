package org.pos.project.possystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;


public class SupplierController {

    @FXML
    private TableView<?> tableView;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    @FXML
    void addBtn(ActionEvent event) {

        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtTel.getText();


        // 01 Create an Insert SQL
        // 02 Run the JDBC driver
        // 03 Create a Connection to the Database
        // 04 Create Statement
        // 05 Execute Statement

        String addNewSupplier = "INSERT INTO supplier VALUES(?,?,?,?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system",
                    "root","Danidu#678@mysql");

            PreparedStatement preparedStatement = connection.prepareStatement(addNewSupplier);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,address);
            preparedStatement.setString(4,contact);

            int result = preparedStatement.executeUpdate();

            if(result >= 0){
                System.out.println("New Supplier Record Added Successfully");
            }else {
                System.out.println("New Supplier Record Not Added Successfully");
            }

        }catch (SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void deleteBtn(ActionEvent event) {

        String id = txtId.getText();

        String deleteNewSupplier = "DELETE FROM supplier WHERE sid=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system",
                    "root","Danidu#678@mysql");

            PreparedStatement preparedStatement = connection.prepareStatement(deleteNewSupplier);
            preparedStatement.setString(1,id);

            int result = preparedStatement.executeUpdate();

            if(result >= 0){
                System.out.println("Supplier Record Deleted Successfully");
            }else {
                System.out.println("Supplier Record Deleted Successfully");
            }

        }catch (SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void searchBtn(ActionEvent event) {

        String id = txtId.getText();

        String addNewSupplier = "SELECT * FROM supplier WHERE sid=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system",
                    "root","Danidu#678@mysql");

            PreparedStatement preparedStatement = connection.prepareStatement(addNewSupplier);
            preparedStatement.setString(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                txtId.setText(resultSet.getString("sid"));
                txtName.setText(resultSet.getString("sname"));
                txtAddress.setText(resultSet.getString("address"));
                txtTel.setText(resultSet.getString("tel"));
            }else {
                System.out.println("Invalid Supplier ID");
            }

        }catch (SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void updateBtn(ActionEvent event) {

        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtTel.getText();

        String updateNewSupplier = "UPDATE supplier SET sname=?, address=?, tel=? WHERE sid=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system",
                    "root","Danidu#678@mysql");

            PreparedStatement preparedStatement = connection.prepareStatement(updateNewSupplier);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,address);
            preparedStatement.setString(3,contact);
            preparedStatement.setString(4,id);

            int result = preparedStatement.executeUpdate();

            if(result >= 0){
                System.out.println("Supplier Record Update Successfully");
            }else {
                System.out.println("Supplier Record Not Update Successfully");
            }

        }catch (SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

}
