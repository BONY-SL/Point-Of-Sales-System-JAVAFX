package org.pos.project.possystem.model;

import org.pos.project.possystem.db.DataBaseConnection;
import org.pos.project.possystem.exception.UserEmailExsist;
import org.pos.project.possystem.tm.User;
import org.pos.project.possystem.util.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {

    private UserModel() {
    }


    public static void registerUser(User user) {

        System.out.println(user);
        String encodedPassword = PasswordEncoder.encodePassword(user.getPassword());
        user.setPassword(encodedPassword);

        String checkEmailExist = "SELECT COUNT(*) FROM users WHERE email = ?";
        String saveNewUser = "INSERT INTO users (email, password, first_name, last_name, user_type) VALUES(?,?,?,?,?)";

        try (Connection connection = DataBaseConnection.getDataBaseConnection().getConnection()) {

            try (PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailExist)) {
                checkEmailStmt.setString(1, user.getEmail());
                ResultSet resultSet = checkEmailStmt.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new UserEmailExsist("Email already exists", "This email is already registered.");
                }
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(saveNewUser)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getFirstName());
                preparedStatement.setString(4, user.getLastName());
                preparedStatement.setString(5, user.getUserType().toString());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void login(User user) {
        System.out.println(user);
    }
}
