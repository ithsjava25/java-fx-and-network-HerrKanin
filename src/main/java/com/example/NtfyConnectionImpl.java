package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Consumer;

public class NtfyConnectionImpl {

    private final HttpClient http = HttpClient.newHttpClient(); //HTTP-klient
    private final String hostName;                              //Serveradress
    private final ObjectMapper mapper = new ObjectMapper();     //JSON-läsare
    private final String senderId;                              // Avsändarens namn

    //Läser serveradress och användarnamn från.env
    public NtfyConnectionImpl() {
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
        senderId = dotenv.get("USER_NAME", System.getProperty("user.name"));
    }

    //Extra konstruktor för tester
    public NtfyConnectionImpl(String hostName) {
        this.hostName = hostName;
        this.senderId = System.getProperty("user.name");
    }

    //Skicka ett textmeddelande
    public void send(String message) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(hostName + "/mytopic"))
                    .header("Content-Type", "text/plain")
                    .header("Title", senderId)              //Skickar vem som skrev
                    .POST(HttpRequest.BodyPublishers.ofString(message))
                    .build();

            http.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error sending message");
        }
    }

    //Tar emot meddelanden som JSON-stream
    public void receive(Consumer<String> messageHandler) {

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(hostName + "/mytopic/json"))
                .GET()
                .build();

        http.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body().forEach(line -> {
                    try {
                        var json = mapper.readTree(line);

                        //Hanterar mottagen fil
                        if (json.has("attachment")) {
                            String filename = json.get("attachment").asText();
                            String receiveSender = json.has("title") ? json.get("title").asText() : "";

                            // Förhindra dubbel visning
                            if (!receiveSender.equals(senderId)) {
                                messageHandler.accept("File mottagen: " + filename);
                            }
                            return;
                        }

                        //Hantera textmeddelande
                        if (json.has("message")) {
                            String msg = json.get("message").asText();
                            String receiveSender = json.has("title") ? json.get("title").asText() : "";

                            //Förhindra dubbel visning
                            if (!receiveSender.equals(senderId)) {
                                messageHandler.accept(msg);
                            }
                        }

                    } catch (Exception _) {
                    }
                }));
    }

    // Skickar en fil som byte-array
    public void sendFile(File file) {
        try {
            byte[] fileBytes = java.nio.file.Files.readAllBytes(file.toPath());

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(hostName + "/mytopic"))
                    .header("Title", senderId)
                    .header("Attachment", file.getName())
                    .POST(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
                    .build();

            http.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            System.out.println("Error sending file");
        }
    }


}