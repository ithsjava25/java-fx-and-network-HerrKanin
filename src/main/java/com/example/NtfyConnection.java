package com.example;

import java.util.function.Consumer;

public abstract interface NtfyConnection {

    Boolean send(String message);
    void receive(Consumer<String> messageHandler);
}

