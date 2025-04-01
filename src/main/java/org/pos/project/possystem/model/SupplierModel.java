package org.pos.project.possystem.model;

import org.pos.project.possystem.db.DataBaseConnection;
import org.pos.project.possystem.dto.SupplierDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SupplierModel {

    private static final Logger logger = Logger.getLogger(SupplierModel.class.getName());

    private SupplierModel() {

    }

    public static int saveSupplier(SupplierDTO supplierDTO){

        String addNewSupplier = "INSERT INTO supplier VALUES(?,?,?,?)";
        int result = 0;

        Connection connection;
        PreparedStatement preparedStatement = null;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();

            preparedStatement = connection.prepareStatement(addNewSupplier);

            preparedStatement.setString(1,supplierDTO.getId());
            preparedStatement.setString(2,supplierDTO.getName());
            preparedStatement.setString(3,supplierDTO.getAddress());
            preparedStatement.setString(4,supplierDTO.getContact());

            result = preparedStatement.executeUpdate();


        }catch (SQLException e){
            logger.info(e.getMessage());
        }finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        return result;

    }

    public static int deleteSupplier(String id){

        String deleteNewSupplier = "DELETE FROM supplier WHERE sid=?";

        Connection connection;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();

            preparedStatement = connection.prepareStatement(deleteNewSupplier);
            preparedStatement.setString(1,id);

            result = preparedStatement.executeUpdate();

        }catch (SQLException e){
            logger.info(e.getMessage());
        }finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        return result;
    }

    public static SupplierDTO searchSupplier(String id){

        String searchSupplier = "SELECT * FROM supplier WHERE sid=?";

        Connection connection;
        PreparedStatement preparedStatement = null;

        SupplierDTO supplierDTO = null;

        try {
             connection = DataBaseConnection.getDataBaseConnection().getConnection();
             preparedStatement = connection.prepareStatement(searchSupplier);
             preparedStatement.setString(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                supplierDTO = new SupplierDTO();
                supplierDTO.setId(resultSet.getString("sid"));
                supplierDTO.setName(resultSet.getString("sname"));
                supplierDTO.setAddress(resultSet.getString("address"));
                supplierDTO.setContact(resultSet.getString("tel"));
            }else {
                logger.info("Invalid Supplier ID");
            }

        }catch (SQLException e){
            logger.info(e.getMessage());
        }finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        return supplierDTO;
    }

    public static int updateSupplier(SupplierDTO supplierDTO){

        String updateNewSupplier = "UPDATE supplier SET sname=?, address=?, tel=? WHERE sid=?";

        Connection connection;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
             connection = DataBaseConnection.getDataBaseConnection().getConnection();
             preparedStatement = connection.prepareStatement(updateNewSupplier);
             preparedStatement.setString(1,supplierDTO.getName());
             preparedStatement.setString(2,supplierDTO.getAddress());
             preparedStatement.setString(3,supplierDTO.getContact());
             preparedStatement.setString(4,supplierDTO.getId());

             result = preparedStatement.executeUpdate();

        }catch (SQLException e){
            logger.info(e.getMessage());
        }finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        return result;
    }

    public static List<SupplierDTO> getAllSuppliers(){

        String getAll = "SELECT * FROM supplier";

        ArrayList<SupplierDTO> supplierArrayList = new ArrayList<>();
        Connection connection;
        PreparedStatement preparedStatement = null;
        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();

            preparedStatement = connection.prepareStatement(getAll);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                SupplierDTO supplierDTO = new SupplierDTO();
                supplierDTO.setId(resultSet.getString(1));
                supplierDTO.setName(resultSet.getString(2));
                supplierDTO.setAddress(resultSet.getString(3));
                supplierDTO.setContact(resultSet.getString(4));
                supplierArrayList.add(supplierDTO);
            }
        }catch (SQLException e){
            logger.info(e.getMessage());
        }finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        return supplierArrayList;
    }
}
