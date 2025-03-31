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
}
