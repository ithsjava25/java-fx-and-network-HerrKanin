package com.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalTime;

public class ChatController {

    //Model som lagrar och validerar meddelanden
    private final ChatModel model = new ChatModel();

    //Hanterar kommunikationen mot ntfy-servern
    private NtfyConnectionImpl ntfy;

    private InputField inputField;

    @FXML
    private TextField textField;

    @FXML
    private VBox chatBox;

    @FXML
    private void initialize() {
        ntfy = new NtfyConnectionImpl();
        inputField = new FxInputField(textField);

        textField.setOnAction(event -> onSendClicked());

        //Lyssnar på inkommande meddelanden från servern
        ntfy.receive(message -> runOnFx(() -> {
            model.addMessage(message);
            addMessageBubble(message, false);
        }));
    }

    @FXML
    public void onSendClicked() {
        //Hämtar text och tar bort whitespace i början & slutet
        String message = inputField.getText().trim();
        if (!model.shouldSendMessage(message)) return;

        ntfy.send(message); //Skick text till ntfy-server
        model.addMessage(message);

        if (chatBox != null) {
            runOnFx(() -> {
                addMessageBubble("Du: " + message, true);
                inputField.clear();
            });
        }
    }

    @FXML
    public void onAttachedClicked() {
        //Öppnar OS filväljare
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Välj en fi att skicka");
        File file = fileChooser.showOpenDialog(null);

        //Skicka vald fli
        if (file != null) {
            ntfy.sendFile(file);
            addMessageBubble("Du bifogade filen: " + file.getName(), true);
        }
    }

    private void addMessageBubble(String text, boolean isUser) {

        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.setPadding(new Insets(8));
        bubble.setMaxWidth(500);

        //Olika färger för användaren vs mottagna meddelanden
        String bgColor = isUser ? "#007AFF" : "#E5E5EA";
        String textColor = isUser ? "white" : "black";

        bubble.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 15; -fx-text-fill: %s; -fx-padding: 8;",
                bgColor, textColor
        ));

        //Visar tid under meddelandebubblan
        LocalTime now = LocalTime.now();
        String timeText = String.format("%02d:%02d", now.getHour(), now.getMinute());
        Label timeLabel = new Label(timeText);
        timeLabel.setStyle("-fx-font-size: 10; -fx-text-fill: gray;");
        timeLabel.setPadding(new Insets(2, 0, 0, 0));

        //paketerar meddelande + tid
        VBox messageBox = new VBox(bubble, timeLabel);
        messageBox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        HBox container = new HBox(messageBox);
        container.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        container.setPadding(new Insets(5));

        chatBox.getChildren().add(container);
    }

    // Dessa metoder används vid testerna
    public void setInputField(InputField field) {
        this.inputField = field;
    }

    public void setChatBox(VBox box) {
        this.chatBox = box;
    }

    public void setNtfyConnection(NtfyConnectionImpl ntfy) {
        this.ntfy = ntfy;
    }

    private static void runOnFx(Runnable task) {
        try {
            if (Platform.isFxApplicationThread()) task.run();
            else Platform.runLater(task);
        } catch (IllegalStateException e) {
            task.run();
        }
    }
}
