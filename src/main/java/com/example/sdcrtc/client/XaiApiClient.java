package com.example.sdcrtc.client;

import com.example.sdcrtc.model.DailyChoice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class XaiApiClient {

    private final RestClient restClient;
    private final String apiKey;

    public XaiApiClient(@Value("${xai.api.key:#{null}}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.x.ai/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + (apiKey != null ? apiKey : ""))
                .build();
    }

    public DailyChoice getDailyChoice() {
        if (apiKey == null || apiKey.isBlank()) {
            return new DailyChoice(LocalDate.now(), "Practice empathy today. Listen to someone with a different viewpoint without interrupting.");
        }

        String systemPrompt = """
                You are a wise, calm, and unifying voice. Your task is to provide one small, practical daily choice that helps reduce tribal contempt, polarization, and us-vs-them thinking.
                The choice must be actionable, positive, and focused on everyday behavior.
                Respond with ONLY 1-3 short sentences. No explanations, no quotes, no lists, no prefixes like "Today's choice:", just the pure choice text.
                """;

        String userPrompt = "What is today's small daily choice to reduce tribal contempt? Date: " + LocalDate.now();

        Map<String, Object> request = Map.of(
                "model", "grok-4.5",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.7,
                "max_tokens", 150
        );

        Map<String, Object> response = restClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(Map.class);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");

        return new DailyChoice(LocalDate.now(), content.trim());
    }
}
