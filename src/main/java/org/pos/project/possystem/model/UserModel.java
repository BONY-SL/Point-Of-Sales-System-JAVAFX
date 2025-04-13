package org.pos.project.possystem.model;

import lombok.Getter;
import lombok.Setter;
import org.pos.project.possystem.db.DataBaseConnection;
import org.pos.project.possystem.exception.UserEmailExsist;
import org.pos.project.possystem.exception.UserNotFound;
import org.pos.project.possystem.tm.Admin;
import org.pos.project.possystem.tm.User;
import org.pos.project.possystem.util.PasswordEncoder;
import org.pos.project.possystem.util.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class UserModel {

    private UserModel() {

    }

    @Getter
    @Setter
    private static User user;

    private static final Logger logger = Logger.getLogger(UserModel.class.getName());



    public static void registerUser(User user) {

        System.out.println(user);
        String encodedPassword = PasswordEncoder.encodePassword(user.getPassword());
        user.setPassword(encodedPassword);

        String checkEmailExist = "SELECT COUNT(*) FROM users WHERE email = ?";
        String saveNewUser = "INSERT INTO users (email, password, first_name, last_name, user_type) VALUES(?,?,?,?,?)";

        Connection connection = DataBaseConnection.getDataBaseConnection().getConnection();
        try{

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
            logger.info(e.getMessage());
        }
    }

    public static boolean login(User user) {

        boolean value = false;
        System.out.println(user);

        String getUserDetails = "SELECT * FROM users WHERE email = ?";
        String getEncryptedPassword;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(getUserDetails);

            preparedStatement.setString(1,user.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                getEncryptedPassword = resultSet.getString(3);
                if(PasswordEncoder.verifyPassword(user.getPassword(),getEncryptedPassword)){

                    User currentUser = new Admin();
                    currentUser.setId(resultSet.getInt(1));
                    currentUser.setEmail(resultSet.getString(2));
                    currentUser.setPassword(user.getPassword());
                    currentUser.setFirstName(resultSet.getString(4));
                    currentUser.setLastName(resultSet.getString(5));
                    currentUser.setUserType(UserType.ADMIN);
                    setUser(currentUser);

                    value = true;
                }else {
                    throw new UserNotFound("Password Error", "Email and Password Not Match");

                }
            }else {
                throw new UserNotFound("User Not Found", "Email Address Not Found");

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
        return value;
    }

    public static User getUserCurrentUser() {
        return user;
    }

}
