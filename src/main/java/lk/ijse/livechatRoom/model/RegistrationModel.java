package lk.ijse.livechatRoom.model;

import lk.ijse.livechatRoom.dbConnection.DbConnection;
import lk.ijse.livechatRoom.dto.RegistrationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationModel {
    public boolean registerUser(RegistrationDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO users VALUES (?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getUser_name());
        ptsm.setString(2, dto.getPassword());

        return ptsm.executeUpdate() > 0;
    }

    public boolean isValidUser(String userName, String pw) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM users WHERE user_name = ? AND password = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, userName);
        ptsm.setString(2,pw);

        ResultSet resultSet = ptsm.executeQuery();

        return resultSet.next();
    }

    public RegistrationDto getUserInfo(String userName) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM users WHERE user_name = ?";
        try (PreparedStatement ptsm = connection.prepareStatement(sql)) {
            ptsm.setString(1, userName);

            try (ResultSet resultSet = ptsm.executeQuery()) {
                if (resultSet.next()) {
                    String retrievedUserName = resultSet.getString("user_name");
                    String retrievedPassword = resultSet.getString("password");

                    return new RegistrationDto();
                }
            }
        }
        return null; // User isn't found
    }

    public boolean check(String userName, String pw) throws SQLException {
        return isValidUser(userName,pw);
    }
}
