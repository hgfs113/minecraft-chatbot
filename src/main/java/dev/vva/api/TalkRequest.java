package dev.vva.api;

import java.util.Map;

public record TalkRequest(String message, Map<String, String> envInfo) {
}
