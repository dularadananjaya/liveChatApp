package lk.ijse.livechatRoom.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import lk.ijse.livechatRoom.dto.RegistrationDto;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SendingFormController {
    @FXML
    private Label lblTime;

    @FXML
    private Label lblUserName;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField txtMassageSend;

    private RegistrationDto userDto;

    @FXML
    private VBox vBox;

    @FXML
    private AnchorPane emojiAnchorpane;

    @FXML
    private GridPane emojiGridpane;

    @FXML
    private ImageView imgEmoji;

    private DataInputStream dataInputStream;

    private DataOutputStream dataOutputStream;

    String mag_updated = "";

    private final String[] emojis = {
            "\uD83D\uDE00", // 😀
            "\uD83D\uDE01", // 😁
            "\uD83D\uDE02", // 😂
            "\uD83D\uDE03", // 🤣
            "\uD83D\uDE04", // 😄
            "\uD83D\uDE05", // 😅
            "\uD83D\uDE06", // 😆
            "\uD83D\uDE07", // 😇
            "\uD83D\uDE08", // 😈
            "\uD83D\uDE09", // 😉
            "\uD83D\uDE0A", // 😊
            "\uD83D\uDE0B", // 😋
            "\uD83D\uDE0C", // 😌
            "\uD83D\uDE0D", // 😍
            "\uD83D\uDE0E", // 😎
            "\uD83D\uDE0F", // 😏
            "\uD83D\uDE10", // 😐
            "\uD83D\uDE11", // 😑
            "\uD83D\uDE12", // 😒
            "\uD83D\uDE13"  // 😓
    };


    public void initialize() {
        setDateAndTime();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), rootNode);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        sendEmojis();

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 3001);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String messageTyp = dataInputStream.readUTF();

//                  SEND TEXT MSG
                    if (messageTyp.equals("TEXT")) {
                        String message = dataInputStream.readUTF();

                        Platform.runLater(() -> {
                            if (mag_updated.equals("done")) {
                                Label label = new Label(message);

                                label.setStyle("-fx-font-size: 15px; -fx-padding: 15px;-fx-font-weight: bold;");
                                label.setBackground(new Background(
                                        new BackgroundFill(Color.rgb(255, 164, 164), new CornerRadii(10), new Insets(10))));

                                BorderPane borderPane = new BorderPane();
                                borderPane.setRight(label);
                                vBox.getChildren().add(borderPane);

                                mag_updated = "";
                            }else {
                                Label label = new Label(message);

                                label.setStyle("-fx-font-size: 15px; -fx-padding: 15px;-fx-font-weight: bold;");
                                label.setBackground(new Background(
                                        new BackgroundFill(Color.WHITE, new CornerRadii(10), new Insets(10))));

                                vBox.getChildren().add(label);
                            }
                        });
//                        SEND IMAGES
                    } else if (messageTyp.equals("IMAGE")) {
                        String message = dataInputStream.readUTF();

                        int file = dataInputStream.readInt();
                        byte [] fileData = new byte[file];
                        dataInputStream.readFully(fileData);

                        Platform.runLater(() -> {
                            // Create an ImageView to display the received image
                            ImageView imageView = new ImageView();
                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(100); // Adjust the width as needed
                            imageView.setFitHeight(100); // Adjust the height as needed

                            try {
                                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);
                                Image image = new Image(byteArrayInputStream);
                                imageView.setImage(image);

                                if (mag_updated.equals("done")) {
                                    Label label = new Label(message);
                                    label.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");
                                    label.setBackground(new Background(new BackgroundFill(Color.rgb(255, 164, 164), new CornerRadii(10), new Insets(10))));
                                    BorderPane borderPane1 = new BorderPane();
                                    borderPane1.setRight(label);

                                    BorderPane borderPane = new BorderPane();
                                    borderPane.setRight(imageView);
                                    vBox.getChildren().add(borderPane1);
                                    vBox.getChildren().add(borderPane);
                                    mag_updated = "";
                                } else {
                                    Label label = new Label(message);
                                    label.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");
                                    label.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), new Insets(10))));
                                    vBox.getChildren().add(label);
                                    vBox.getChildren().add(imageView);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.INFORMATION, "Connection Closed").show();
            }
        }).start();
    }

    private void sendEmojis() {
        emojiAnchorpane.setVisible(false);
        int imageViewIndex = 0;
        for (int i = 0; i < 4; i++) { // rows
            for (int j = 0; j < 4; j++) { // columns
                if (imageViewIndex < emojis.length) {
                    String emoji = emojis[imageViewIndex];
                    JFXButton emojiButton = createEmojiButton(emoji);
                    emojiGridpane.add(emojiButton,j,i);
                    imageViewIndex++;
                } else {
                    break;
                }
            }
        }
    }

    private JFXButton createEmojiButton(String emoji) {
        JFXButton button = new JFXButton(emoji);
        button.getStyleClass().add("emoji-button");
        button.setOnAction(this::emojiButtonAction);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(button, true);
        GridPane.setFillHeight(button, true);
        button.setStyle("-fx-font-size: 15; -fx-text-fill: black; -fx-background-color: #F0F0F0; -fx-border-radius: 50");
        return button;
    }

    private void emojiButtonAction(ActionEvent actionEvent) {
        JFXButton button = (JFXButton) actionEvent.getSource();
        txtMassageSend.appendText(button.getText());
    }

    @FXML
    void txtMessageSendOnAction(ActionEvent event) {
        String sender = lblUserName.getText();
        String message = txtMassageSend.getText().trim(); // Trim to remove leading/trailing spaces

        try {
            dataOutputStream.writeUTF("TEXT");
            dataOutputStream.writeUTF(sender+":" +"\n"+ message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mag_updated = "done";
    }

    @FXML
    void imgSendImagesOnAction(MouseEvent event) {
        String sender = lblUserName.getText();
        mag_updated = "done";
        Platform.runLater(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                    new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"));

            // Show file chooser dialog
            File selectedFiles = fileChooser.showOpenDialog(null);

            // Process selected file
            if (selectedFiles != null) {
                try {
                    // Read the image file as bytes
                    byte [] fileData = Files.readAllBytes(selectedFiles.toPath());

                    // Send the image file to the server
                    dataOutputStream.writeUTF("IMAGE");
//                    dataOutputStream.writeUTF(sender);
                    dataOutputStream.writeInt(fileData.length);
                    dataOutputStream.write(fileData);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void btnSendEmojiOnAction(ActionEvent event) {
        emojiAnchorpane.setVisible(!emojiAnchorpane.isVisible());
    }

    public void setUser(RegistrationDto registrationDto) {
        this.userDto = registrationDto;
        loadUserName();
    }

    private void loadUserName() {
        if (userDto != null) {
            String userName = userDto.getUser_name();
            lblUserName.setText(userName);
        }
    }

    @FXML
    void imgBackOnAction(MouseEvent event) throws IOException {
        System.exit(0);
    }

    private void setDateAndTime(){
        Platform.runLater(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
                String timeNow = LocalTime.now().format(formatter);
                lblTime.setText(timeNow);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
    }
}
