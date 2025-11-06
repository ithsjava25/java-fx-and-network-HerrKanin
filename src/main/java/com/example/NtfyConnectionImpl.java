package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Consumer;

public class NtfyConnectionImpl{

    private final HttpClient http = HttpClient.newHttpClient();
    private final String hostName;
    private final ObjectMapper mapper = new ObjectMapper();

    public NtfyConnectionImpl() {
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
    }

    public NtfyConnectionImpl(String hostName) {
        this.hostName = hostName;
    }

    public void send(String message) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(hostName + "/mytopic"))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();
        try {
            http.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error sending message");
        }
    }

    public void receive(Consumer<String> messageHandler) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(hostName + "/mytopic/json"))
                .GET()
                .build();

        http.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body().forEach(line -> {
                    try {
                        var json = mapper.readTree(line);
                        if (json.has("message")) {
                            messageHandler.accept(json.get("message").asText());
                        }
                    } catch (Exception _) {}
                }));
    }


}