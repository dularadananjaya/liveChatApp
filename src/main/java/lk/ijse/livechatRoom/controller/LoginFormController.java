package lk.ijse.livechatRoom.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.livechatRoom.dto.RegistrationDto;
import lk.ijse.livechatRoom.model.RegistrationModel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginFormController {

    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private AnchorPane rootNode;
    private RegistrationModel registrationModel = new RegistrationModel();
    private static ArrayList<DataOutputStream> clientList = new ArrayList<>();

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String name = txtUser.getText();
        String pw = txtPassword.getText();

        try {
            boolean isValid = registrationModel.isValidUser(name,pw);
            if (isValid) {

                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ServerForm.fxml"));
                AnchorPane anchorPane = loader.load();

                // Create a new scene with the loaded anchorPane
                Scene scene = new Scene(anchorPane);

                // Get the current stage
                Stage stage = new Stage();

                // Set the new scene to the current stage
                stage.setScene(scene);

                // Customize the stage properties
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.setTitle("Echo Room");
                stage.show();

                // Retrieve user information
                RegistrationDto userDto = registrationModel.getUserInfo(name);

                // Access the controller from the FXMLLoader
                SendingFormController messageFormController = loader.getController();

                // Pass user information to the controller
                messageFormController.setUser(userDto);
                txtUser.setText("");
                txtPassword.setText("");

            } else {
                new Alert(Alert.AlertType.ERROR,"User Name And Password Did Not Matched try again").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(3001);
                Socket socket;
                while (true) {
                    System.out.println("Waiting for clients...");
                    socket = serverSocket.accept();
                    System.out.println("Accepted...");
                    Client clients = new Client(socket,clientList);
                    new Thread(clients).start();

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void txtLoginOnAction(ActionEvent event) throws IOException {
        btnLoginOnAction(new ActionEvent());
    }

    @FXML
    void hyperSignUpOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/RegisterForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Register");
    }

    @FXML
    void txtGoToPasswordOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }
}
