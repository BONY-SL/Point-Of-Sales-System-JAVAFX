package org.pos.project.possystem.model;

import org.pos.project.possystem.db.DataBaseConnection;
import org.pos.project.possystem.dto.OrderDetailsDTO;
import org.pos.project.possystem.dto.TransactionDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderModel {

    private PlaceOrderModel() {

    }

    public static Integer generateOrderId() {

        int count = 0;

        try {
            Connection connection = DataBaseConnection.getDataBaseConnection().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM transaction");

            while (resultSet.next()){
                count = resultSet.getInt(1);

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count + 1;
    }

    public static List<OrderDetailsDTO> searchOrderById(int orderId) throws SQLException{

        List<OrderDetailsDTO> orderDetailsDTOList = new ArrayList<>();

        Connection connection = DataBaseConnection.getDataBaseConnection().getConnection();

        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM orderdetails WHERE orderDetails_id = ?");
        ps.setInt(1, orderId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int productId = rs.getInt("product_id");
            int qty = rs.getInt("qty");
            orderDetailsDTOList.add(new OrderDetailsDTO(orderId, productId, qty));
        }
        return orderDetailsDTOList;
    }

    // Haddle transaction
    public static boolean placeOrder(TransactionDTO transactionDTO) throws SQLException {

        Connection connection = DataBaseConnection.getDataBaseConnection().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement psTm = connection.prepareStatement("INSERT INTO transaction VALUE(?, ?)");
            psTm.setInt(1, transactionDTO.getTransactionId());
            psTm.setObject(2, transactionDTO.getOrderDate());

            boolean isOrderAdd = psTm.execute();

            if (!isOrderAdd){
                boolean isOrderDetailAdd = addOrderDetails(transactionDTO.getOrderDetailList());
                if(isOrderDetailAdd){
                    boolean isUpdateStock = ProductStockModel.updateStock(transactionDTO.getOrderDetailList());
                    if (isUpdateStock){
                        connection.commit();
                        System.out.println("Commit Successful");
                        return true;
                    }
                }
            }

            connection.rollback();
            System.out.println("Rollback Executed");
            return false;

        } catch (SQLException e) {

            connection.rollback();
            System.out.println("rollback");
            throw new RuntimeException(e);

        } finally {

            System.out.println("finally");
            connection.setAutoCommit(true);

        }
    }

    public static boolean addOrderDetails(List<OrderDetailsDTO> orderDetailList) {
        try {
            boolean isAdded = true;
            for (OrderDetailsDTO orderDetail : orderDetailList) {
                boolean result =  execute(
                        "INSERT INTO orderdetails VALUES(?, ?, ?)",
                        orderDetail.getTransactionId(),
                        orderDetail.getPrductId(),
                        orderDetail.getQty()

                );
                if (!result) {
                    isAdded = false;
                    break;
                }
            }
            return isAdded;
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
