package com.example;

import java.util.ArrayList;
import java.util.List;

public class ChatModel {

    private final List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        if (message == null || message.isBlank())
            return;
        messages.add(message);
    }

    public List<String> getMessages() {
        return List.copyOf(messages);
    }

    public int getMessageCount() {
        return messages.size();
    }
    public boolean shouldSendMessage(String message){
        return message != null && !message.isBlank();
    }

}
