package org.pos.project.possystem.db;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DataBaseConnection {

    private static String dbUrl;

    private static String dbUserName;

    private static String dbPassword;


    private static DataBaseConnection dataBaseConnection = null;

    private static Connection connection = null;

    private static final Logger logger = Logger.getLogger(DataBaseConnection.class.getName());

    private DataBaseConnection() {

        registerConnection();
    }

    private static void registerConnection(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.info(e.getMessage());
        }
        try {
            loadDatabaseProperties();
            connection = DriverManager.getConnection(dbUrl,
                    dbUserName,dbPassword);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }

    }

    private static void loadDatabaseProperties() {

        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);

            dbUrl = properties.getProperty("db.url");
            dbUserName = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");

        } catch (IOException e) {
            logger.info("Error loading properties: " + e.getMessage());
        }
    }

    public static DataBaseConnection getDataBaseConnection(){

        if (dataBaseConnection == null) {
            dataBaseConnection = new DataBaseConnection();
        }
        return dataBaseConnection;

    }

    public  Connection getConnection(){
        return connection;
    }

}
