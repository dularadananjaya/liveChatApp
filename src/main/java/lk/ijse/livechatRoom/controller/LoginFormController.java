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
import lk.ijse.livechatRoom.model.RegistrationModel;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private AnchorPane rootNode;
    private RegistrationModel registrationModel = new RegistrationModel();

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String userName = txtUser.getText();
        String pw = txtPassword.getText();

        try {
            boolean isValid = registrationModel.isValidUser(userName,pw);
            if (isValid){

                registrationModel.getUserInfo(userName); //Create a method in the model where the query is executed
                rootNode.getScene().getWindow().hide();
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClientForm.fxml"));
                Parent root = loader.load();

                ClientFormController controller = new ClientFormController();
                controller.setClientName(txtUser.getText()); // Set the parameter
                loader.setController(controller);


                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle(txtUser.getText());
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
                primaryStage.setOnCloseRequest(windowEvent -> {
                    controller.shutdown();
                });
                primaryStage.show();

                txtUser.clear();
            }else {
                new Alert(Alert.AlertType.ERROR,"User Name And Password Did Not Matched try again").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
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
