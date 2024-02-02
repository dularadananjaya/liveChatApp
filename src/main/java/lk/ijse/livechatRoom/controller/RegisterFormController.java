package lk.ijse.livechatRoom.controller;

import lk.ijse.livechatRoom.dto.RegistrationDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.livechatRoom.model.RegistrationModel;
import lk.ijse.livechatRoom.regExPatterns.RegExpatterns;
import lombok.var;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterFormController {

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUser;

    @FXML
    private AnchorPane RegisterPane;

    private RegistrationModel registrationModel = new RegistrationModel();

    private void clearFields() {
        txtUser.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/LoginForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.RegisterPane.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        String userName = txtUser.getText();
        String pw = txtPassword.getText();
        String ConfirmPW = txtConfirmPassword.getText();

        boolean isUserValid = RegExpatterns.getValidName().matcher(userName).matches();
        boolean isPasswordValid = RegExpatterns.getValidPassword().matcher(pw).matches();

        if (!isUserValid){
            new Alert(Alert.AlertType.ERROR,"Can Not Leave Name Empty").showAndWait();
            return;
        }if (!isPasswordValid){
            new Alert(Alert.AlertType.ERROR,"Password need to contain minimum of four Characters").showAndWait();
            return;
        }if (!ConfirmPW.equals(pw)){
            new Alert(Alert.AlertType.ERROR,"Password Did Not Matched").showAndWait();
        }else {
            var dto = new RegistrationDto(userName, pw);
            try {
                boolean checkDuplicates = registrationModel.check(userName, pw);
                if (checkDuplicates) {
                    new Alert(Alert.AlertType.ERROR, "Duplicate Entry").showAndWait();
                    return;
                }
                boolean isRegistered = registrationModel.registerUser(dto);
                if (isRegistered) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Your Account Has been Created").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void txtRegisterOnAction(ActionEvent event) {
        btnRegisterOnAction(new ActionEvent());
    }

    @FXML
    void txtGoToNewPasswordOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    void txtGoToConfirmPasswordOnAction(ActionEvent event) {
        txtConfirmPassword.requestFocus();
    }

}
