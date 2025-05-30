package dev.vva.api;

import com.google.gson.Gson;
import dev.vva.Mymod;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ApiService {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final Gson GSON = new Gson();

    private final String baseUrl = "http://localhost:8080/generate";

    public void sendMessage(TalkRequest talkRequest, Consumer<String> callback) {
        CompletableFuture.runAsync(() -> {
            try {
                var requestBodyJson = GSON.toJson(talkRequest, TalkRequest.class);
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(30))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                        .build();

                var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    callback.accept(response.body());
                } else {
                    Mymod.LOGGER.error("API request failed with status code: {}", response.statusCode());
                    callback.accept(null);
                }

            } catch (Exception e) {
                Mymod.LOGGER.error("Failed to communicate with API server", e);
                callback.accept(null);
            }
        });
    }
}
