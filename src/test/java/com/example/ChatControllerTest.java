package com.example;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;

class ChatControllerTest {

    private ChatController controller;
    private NtfyConnectionImpl mockConnection;

    @BeforeAll
    static void initJFX() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
    }

    @BeforeEach
    void setUp() {
        controller = new ChatController();
        mockConnection = Mockito.mock(NtfyConnectionImpl.class);
        controller.setNtfyConnection(mockConnection);

        controller.inputField = new TextField();
        controller.setChatBox(new VBox());
    }

    @Test
    void shouldSendMessageWhenUserClicksSend() {
        //Arrange
        String message = "Hej Världen";
        controller.setInputField(new TextField(message));
        //Act
        controller.onSendClicked();
        //Assert
        verify(mockConnection, times(1)).send(message);
    }

    @Test
    void shouldNotSendWhenMessageIsEmpty() {
        //Assert
        controller.setInputField(new TextField("   "));
        //Act
        controller.onSendClicked();
        //Assert
        verify(mockConnection, never()).send(anyString());
    }

}