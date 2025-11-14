package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class ChatControllerTest {

    private ChatController controller;
    private NtfyConnectionImpl mockConn;

    static class FakeInputField implements InputField {

        String text = "";

        @Override public String getText() {
            return text;
        }

        @Override public void clear() {
            text = "";
        }
    }

    @BeforeEach
    void setup() {
        controller = new ChatController();
        mockConn = Mockito.mock(NtfyConnectionImpl.class);

        controller.setNtfyConnection(mockConn);
        controller.setChatBox(null);
    }

    @Test
    void shouldSendMessageWhenUserClicksSend() {
        FakeInputField f = new FakeInputField();
        f.text = "Hej Världen";
        controller.setInputField(f);

        controller.onSendClicked();

        verify(mockConn, times(1)).send("Hej Världen");
    }

    @Test
    void shouldNotSendWhenEmpty() {
        FakeInputField f = new FakeInputField();
        f.text = "  ";
        controller.setInputField(f);

        controller.onSendClicked();

        verify(mockConn, never()).send(anyString());
    }
}
