package org.pos.project.possystem.model;

import lombok.Getter;
import lombok.Setter;
import org.pos.project.possystem.db.DataBaseConnection;
import org.pos.project.possystem.dto.UserDTO;
import org.pos.project.possystem.exception.UserEmailExsist;
import org.pos.project.possystem.exception.UserNotFound;
import org.pos.project.possystem.tm.Admin;
import org.pos.project.possystem.tm.User;
import org.pos.project.possystem.util.PasswordEncoder;
import org.pos.project.possystem.util.SessionManager;
import org.pos.project.possystem.util.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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


    public static List<UserDTO> getAllUsers(){

        String getAll = "SELECT * FROM users";

        ArrayList<UserDTO> userDTOArrayList = new ArrayList<>();

        Connection connection;
        PreparedStatement preparedStatement = null;

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(getAll);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                var userDTO = UserDTO.builder()
                        .id(resultSet.getInt(1))
                        .email(resultSet.getString(2))
                        .firstName(resultSet.getString(4))
                        .lastName(resultSet.getString(5))
                        .userType(UserType.valueOf(resultSet.getString(6)))
                        .build();

                userDTOArrayList.add(userDTO);
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
        return userDTOArrayList;
    }

    public static int updateUserDetails(UserDTO userDto) {

        User currentUser = SessionManager.getCurrentUser();

        Connection connection;
        PreparedStatement preparedStatement = null;
        String checkEmailQuery = "SELECT user_id FROM users WHERE email = ? AND user_id != ?";

        try {
            connection = DataBaseConnection.getDataBaseConnection().getConnection();
            preparedStatement = connection.prepareStatement(checkEmailQuery);

            preparedStatement.setString(1, userDto.getEmail());
            preparedStatement.setInt(2, currentUser.getId());

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return -1;
            }

            String updateQuery = "UPDATE users SET email = ?, password = ? WHERE user_id = ?";
            String encodedPassword = PasswordEncoder.encodePassword(userDto.getPassword());

            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {


                stmt.setString(1, userDto.getEmail());
                stmt.setString(2, encodedPassword);
                stmt.setInt(3, currentUser.getId());
                SessionManager.getCurrentUser().setEmail(userDto.getEmail());
                SessionManager.getCurrentUser().setPassword(userDto.getPassword());

                return stmt.executeUpdate();
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


        System.out.println(userDto);
        return 0;
    }
}
