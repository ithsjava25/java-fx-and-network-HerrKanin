package com.example;

import java.util.ArrayList;
import java.util.List;

public class ChatModel {
    //Lista som lagrar alla meddelanden som skickas eller tas emot
    private final List<String> messages = new ArrayList<>();

    //Lägger till nytt meddelande om det inte är null eller tomt
    public void addMessage(String message) {
        if (message == null || message.isBlank())
            return; //Avbryter om meddelanden är ogiltigt
        messages.add(message);
    }

    public List<String> getMessages() {
        return List.copyOf(messages);
    }

    //Returnerar hut många meddelanden som lagrats
    public int getMessageCount() {
        return messages.size();
    }

    // Hjälpmetod som avgör om ett meddelande är giltigt att skicka.
    public boolean shouldSendMessage(String message){
        return message != null && !message.isBlank();
    }

}
