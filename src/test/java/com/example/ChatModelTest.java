package com.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChatModelTest {

    @Test
    void addMessagesToList(){
        ChatModel model = new ChatModel();
        model.addMessage("Hej");
        model.addMessage("Världen");

        assertThat(model.getMessages())
                .containsExactly("Hej", "Världen");
    }

    @Test
    void ignoresBlankMessages(){
        ChatModel model = new ChatModel();
        model.addMessage(" ");
        model.addMessage("  ");

        assertThat(model.getMessages()).isEmpty();
    }

    @Test
    void returnCorrectMessageCount(){
        ChatModel model = new ChatModel();
        model.addMessage("Hej");

        assertThat(model.getMessageCount()).isEqualTo(1);
    }

}