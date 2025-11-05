package com.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalTime;

public class ChatController {

    private NtfyConnectionImpl ntfy;

    @FXML
    private VBox chatBox;

    @FXML
    private TextField inputField;

    @FXML
    public void initialize() {
        inputField.setOnAction((event) -> onSendClicked());

        ntfy = new NtfyConnectionImpl();

        ntfy.receive(message -> Platform.runLater(() -> addMessageBubble(message, false)));
    }

    @FXML
    private void onSendClicked() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            addMessageBubble(message, true);
            inputField.clear();
        }
        ntfy.send(message);

    }

    private void addMessageBubble (String text, boolean isUser) {

        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.setPadding(new Insets(8));
        bubble.setMaxWidth(500);

        String bgColor = isUser ? "#007AFF" : "#E5E5EA";
        String textColor = isUser ? "white" : "black";

        bubble.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 15; -fx-text-fill: %s; -fx-padding: 8;",
                bgColor, textColor
        ));

        LocalTime now = LocalTime.now();
        String timeText = String.format("%02d:%02d", now.getHour(), now.getMinute());
        Label timeLabel = new Label(timeText);
        timeLabel.setStyle("-fx-font-size: 10; -fx-text-fill: gray;");
        timeLabel.setPadding(new Insets(2, 0, 0, 0));

        VBox messageBox = new VBox(bubble, timeLabel);
        messageBox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        HBox container = new HBox(messageBox);
        container.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        container.setPadding(new Insets(5));

        chatBox.getChildren().add(container);
    }
}
