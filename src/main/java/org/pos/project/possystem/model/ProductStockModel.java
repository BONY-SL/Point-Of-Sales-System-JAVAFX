package org.pos.project.possystem.model;

import org.pos.project.possystem.db.DataBaseConnection;
import org.pos.project.possystem.dto.OrderDetailsDTO;
import org.pos.project.possystem.dto.ProductStockDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ProductStockModel {

    private static final Logger logger = Logger.getLogger(ProductStockModel.class.getName());


    private ProductStockModel() {

    }

    public static List<Integer> getAllProductId(){

        String getProductId = "SELECT product_id FROM products";

        ArrayList<Integer> productIdArrayList = new ArrayList<>();

        Connection connection;
        PreparedStatement preparedStatement = null;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(getProductId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                productIdArrayList.add(resultSet.getInt(1));
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
        return productIdArrayList;
    }

    // get All Stock Details
    public static List<ProductStockDTO> getAllProductStock(){
        String getAll = "SELECT * FROM product_quantity";

        ArrayList<ProductStockDTO> productStockDTOS = new ArrayList<>();
        Connection connection;
        PreparedStatement preparedStatement = null;
        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();

            preparedStatement = connection.prepareStatement(getAll);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                ProductStockDTO productStockDTO = ProductStockDTO.builder()
                        .id(resultSet.getInt(1))
                        .productId(resultSet.getInt(2))
                        .quantity(resultSet.getDouble(3))
                        .updateTime(resultSet.getTimestamp(4).toLocalDateTime())
                        .build();
                productStockDTOS.add(productStockDTO);
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
        return productStockDTOS;
    }

    public static int saveNewStock(ProductStockDTO newStock) {

        String addNewStock = "INSERT INTO product_quantity (product_id,quantity) VALUES(?,?)";
        String updateQuantity = "UPDATE product_quantity SET quantity = ? WHERE product_id = ?";
        String productIsAlreadyAvailable = "SELECT * FROM product_quantity WHERE product_id = ?";

        int result = 0;

        Connection connection;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2;
        PreparedStatement preparedStatement3;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(productIsAlreadyAvailable);

            preparedStatement.setInt(1, newStock.getProductId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // if it Exsist
                preparedStatement2 = connection.prepareStatement(updateQuantity);
                double quantity = resultSet.getDouble(3) + newStock.getQuantity();
                preparedStatement2.setDouble(1, quantity);
                preparedStatement2.setInt(2,newStock.getProductId());
                result = preparedStatement2.executeUpdate();

                preparedStatement2.close();

            }else {
                // if it not Exsist
                preparedStatement3 = connection.prepareStatement(addNewStock);
                preparedStatement3.setInt(1,newStock.getProductId());
                preparedStatement3.setDouble(2,newStock.getQuantity());
                result = preparedStatement3.executeUpdate();

                preparedStatement3.close();
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
        return result;
    }

    public static ProductStockDTO getStockDetails(Integer productID){

        String getStockDetails = "SELECT * FROM product_quantity WHERE product_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ProductStockDTO productStockDTO = null;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(getStockDetails);
            preparedStatement.setInt(1,productID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                productStockDTO = ProductStockDTO.builder()
                        .id(resultSet.getInt(1))
                        .productId(resultSet.getInt(2))
                        .quantity(resultSet.getDouble(3))
                        .updateTime(resultSet.getTimestamp(4).toLocalDateTime())
                        .build();
            }else {
                return null;
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
        return productStockDTO;
    }

    public static int deleteProductStock(Integer id) {

        String deleteProductStock = "DELETE FROM product_quantity WHERE id=?";

        Connection connection;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();

            preparedStatement = connection.prepareStatement(deleteProductStock);
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

    public static boolean updateStock(List<OrderDetailsDTO> orderDetailList) {
        try {
            for (OrderDetailsDTO orderDetail : orderDetailList) {
                boolean result = execute(
                        "UPDATE product_quantity SET quantity = quantity - ? WHERE product_id = ?",
                        orderDetail.getQty(),
                        orderDetail.getPrductId()
                );
                if (!result) {
                    return false;
                }
            }
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T execute(String sql, Object... args) throws SQLException, ClassNotFoundException {
        PreparedStatement psTm = DataBaseConnection.getDataBaseConnection().getConnection().prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            psTm.setObject((i + 1), args[i]);
        }
        if (sql.startsWith("SELECT") || sql.startsWith("select")) {
            return (T) psTm.executeQuery();
        }
        return (T) (Boolean) (psTm.executeUpdate() > 0);
    }
}
