package dev.vva;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

    public void sendMessage(String message,  Consumer<String> callback) {
        CompletableFuture.runAsync(() -> {
            try {
                var requestBody = new JsonObject();
                requestBody.addProperty("prompt", message);
                var requestBodyJson = GSON.toJson(requestBody);
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(15))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                        .build();

                var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    callback.accept(response.body());
//                    JsonObject jsonResponse = GSON.fromJson(response.body(), JsonObject.class);
//                    if (jsonResponse.has("response")) {
//                        String villagerResponse = jsonResponse.get("response").getAsString();
//                        callback.accept(villagerResponse);
//                    } else {
//                        Mymod.LOGGER.error("API response missing 'response' field: {}", response.body());
//                        callback.accept(null);
//                    }
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
