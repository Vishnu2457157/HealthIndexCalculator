
package ai;

import okhttp3.*;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

public class AIService {
    private static final String GITHUB_TOKEN = System.getenv("GITHUB_TOKEN");

    private static final String CHAT_ENDPOINT = System.getenv("GITHUB_MODELS_ENDPOINT") != null
            ? System.getenv("GITHUB_MODELS_ENDPOINT")
            : "https://models.github.ai/inference/chat/completions";

    private static final String CATALOG_ENDPOINT = "https://models.github.ai/catalog/models";

    private static final String MODEL = (System.getenv("GITHUB_MODEL") != null
            && !System.getenv("GITHUB_MODEL").isEmpty())
            ? System.getenv("GITHUB_MODEL")
            : null; // resolve at runtime if null

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(20))
            .readTimeout(Duration.ofSeconds(60))
            .writeTimeout(Duration.ofSeconds(30))
            .build();

    public static String generateResponse(String prompt) throws IOException {
        if (GITHUB_TOKEN == null || GITHUB_TOKEN.isEmpty()) {
            throw new IllegalStateException("GITHUB_TOKEN env var not set");
        }
        Objects.requireNonNull(prompt, "prompt");

        final String modelId = (MODEL != null) ? MODEL : pickModel();

        String bodyJson = "{\n" +
                "  \"model\": \"" + modelId + "\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"You are a helpful QA data assistant. Return only valid JSON arrays when asked to generate data.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"" + escape(prompt) + "\"}\n" +
                "  ],\n" +
                "  \"temperature\": 0.2\n" +
                "}";

        Request request = new Request.Builder()
                .url(CHAT_ENDPOINT)
                .addHeader("Authorization", "Bearer " + GITHUB_TOKEN)
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(bodyJson, JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            final String resp = (response.body() != null ? response.body().string() : "");
            if (!response.isSuccessful()) {
                throw new IOException("GitHub Models API error. HTTP "
                        + response.code() + " " + response.message() + "\n" + resp);
            }
            return resp; // OpenAI-style JSON with choices[].message.content
        }
    }

    private static String pickModel() throws IOException {
        Request req = new Request.Builder()
                .url(CATALOG_ENDPOINT)
                .addHeader("Authorization", "Bearer " + GITHUB_TOKEN)
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .get()
                .build();

        try (Response res = client.newCall(req).execute()) {
            final String cat = res.body() != null ? res.body().string() : "";
            if (!res.isSuccessful()) {
                throw new IOException("Catalog error HTTP " + res.code() + ": " + cat);
            }
            // TODO: parse JSON properly with Jackson; for brevity, prefer openai/gpt-4.1
            if (cat.contains("\"id\":\"openai/gpt-4.1\"")) return "openai/gpt-4.1";
            // Add more fallbacks here, e.g., "mistralai/mistral-small", etc., based on your catalog.
            throw new IOException("No preferred model found in catalog. Response:\n" + cat);
        }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
