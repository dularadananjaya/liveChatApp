package lk.ijse.livechatRoom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.livechatRoom.controller.LoginFormController;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/LoginForm.fxml"))));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("Login to LiveChat");
        stage.show();
        LoginFormController.startServer();
    }
}

