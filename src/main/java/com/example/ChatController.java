package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private void onSendClicked() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            chatArea.appendText("\nDu: " + message);
            inputField.clear();
        }

    }
}
