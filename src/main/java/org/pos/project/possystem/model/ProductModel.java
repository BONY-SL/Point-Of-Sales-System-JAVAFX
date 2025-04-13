package org.pos.project.possystem.model;

import org.pos.project.possystem.db.DataBaseConnection;
import org.pos.project.possystem.dto.ProductDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductModel {

    private static final Logger logger = Logger.getLogger(ProductModel.class.getName());

    private ProductModel() {
    }

    public static List<String> getSuppliersId(){

        String getAll = "SELECT sid FROM supplier";

        ArrayList<String> supplierArrayList = new ArrayList<>();
        Connection connection;
        PreparedStatement preparedStatement = null;
        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();

            preparedStatement = connection.prepareStatement(getAll);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                supplierArrayList.add(resultSet.getString(1));
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

    public static int saveNewProduct(ProductDTO productDTO){

        String addNewProduct = "INSERT INTO products (name,description,unit_price,supplier_id) VALUES (?,?,?,?)";

        int result = 0;

        Connection connection;
        PreparedStatement preparedStatement = null;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(addNewProduct);

            preparedStatement.setString(1,productDTO.getName());
            preparedStatement.setString(2,productDTO.getDescription());
            preparedStatement.setDouble(3,productDTO.getUnitPrice());
            preparedStatement.setString(4,productDTO.getSupplierId());

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

    public static int deleteProduct(Integer id){

        String deleteProduct = "DELETE FROM products WHERE product_id=?";

        Connection connection;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();

            preparedStatement = connection.prepareStatement(deleteProduct);
            preparedStatement.setInt(1,id);

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

    public static List<ProductDTO> getAllProducts(){

        String getAll = "SELECT * FROM products";

        ArrayList<ProductDTO> productDTOArrayList = new ArrayList<>();

        Connection connection;
        PreparedStatement preparedStatement = null;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(getAll);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                var product = ProductDTO.builder()
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .description(resultSet.getString(3))
                        .unitPrice(resultSet.getDouble(4))
                        .supplierId(resultSet.getString(5)).build();

                productDTOArrayList.add(product);
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

        return productDTOArrayList;

    }

    public static int updateProductDetails(ProductDTO product) {

        String updateNewSupplier = "UPDATE products SET name=?, description=?, unit_price=?, supplier_id=? WHERE product_id=?";

        Connection connection;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(updateNewSupplier);
            preparedStatement.setString(1,product.getName());
            preparedStatement.setString(2,product.getDescription());
            preparedStatement.setDouble(3,product.getUnitPrice());
            preparedStatement.setString(4,product.getSupplierId());
            preparedStatement.setInt(5,product.getId());
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


    public static ProductDTO getProductByID(Integer id) {

        String getProductDetails = "SELECT * FROM products WHERE product_id = ? ";

        Connection connection;
        PreparedStatement preparedStatement = null;

        ProductDTO productDTO = null;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();

            preparedStatement = connection.prepareStatement(getProductDetails);
            preparedStatement.setInt(1,id);

            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {

                productDTO = ProductDTO.builder()
                        .id(result.getInt(1))
                        .name(result.getString(2))
                        .description(result.getString(3))
                        .unitPrice(result.getDouble(4))
                        .supplierId(result.getString(5))
                        .build();
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
        return productDTO;
    }
}
