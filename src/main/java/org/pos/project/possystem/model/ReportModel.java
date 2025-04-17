package org.pos.project.possystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pos.project.possystem.db.DataBaseConnection;
import org.pos.project.possystem.dto.ProductQtyReportDTO;

import java.sql.*;

public class ReportModel {

    private ReportModel() {
    }

    public static ObservableList<ProductQtyReportDTO> getProQtyByDate(Date sqlDate) {

        String query = "SELECT t.date, od.product_id, SUM(od.qty) AS " +
                "total_quantity FROM orderdetails od JOIN " +
                "transaction t ON od.transaction_id = t.transaction_id " +
                "WHERE t.date = ? GROUP BY t.date, od.product_id";

        ObservableList<ProductQtyReportDTO> productQty = FXCollections.observableArrayList();
        try{ Connection conn = DataBaseConnection.getDataBaseConnection().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setDate(1, sqlDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                productQty.add(new ProductQtyReportDTO(rs.getDate("date"),
                        rs.getInt("product_id"),
                        rs.getInt("total_quantity"))
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return productQty;
    }
}
