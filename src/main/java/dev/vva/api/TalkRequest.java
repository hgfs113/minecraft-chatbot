package dev.vva.api;

import java.util.Map;

public record TalkRequest(String conversationId,
                          String message,
                          Map<String, String> envInfo,
                          String mood) {
}
