
package ai;

import models.HealthDataRow;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class TestDataMapper {

    /**
     * Expects OpenAI chat/completions JSON.
     * The model's text (message.content) must be a JSON array like:
     * [ {"age":25,"pulseRate":72,"bloodPressure":120}, ... ]
     */
    public static List<HealthDataRow> mapFromOpenAIResponse(String apiJson) {
        JsonObject root = JsonParser.parseString(apiJson).getAsJsonObject();
        JsonArray choices = root.getAsJsonArray("choices");
        if (choices == null || choices.size() == 0) {
            throw new IllegalStateException("No choices in OpenAI response");
        }

        String content = choices.get(0).getAsJsonObject()
                .get("message").getAsJsonObject()
                .get("content").getAsString();

        // Strip code fences if the model returned ```json ... ```
        content = stripCodeFences(content);

        JsonArray arr = JsonParser.parseString(content).getAsJsonArray();

        List<HealthDataRow> rows = new ArrayList<>();
        for (JsonElement e : arr) {
            JsonObject o = e.getAsJsonObject();
            int age = getInt(o, "age");
            int pulse = getInt(o, "pulseRate");
            int bp = getInt(o, "bloodPressure");
            rows.add(new HealthDataRow(age, pulse, bp));
        }
        return rows;
    }

    private static String stripCodeFences(String s) {
        String trimmed = s.trim();
        if (trimmed.startsWith("```")) {
            // remove leading ```(json)?
            int firstNL = trimmed.indexOf('\n');
            if (firstNL > 0) trimmed = trimmed.substring(firstNL + 1);
            // remove trailing ```
            int lastFence = trimmed.lastIndexOf("```");
            if (lastFence >= 0) trimmed = trimmed.substring(0, lastFence);
            return trimmed.trim();
        }
        return s;
    }

    private static int getInt(JsonObject o, String key) {
        JsonElement e = o.get(key);
        if (e == null || e.isJsonNull()) return 0;
        if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber()) {
            return e.getAsInt();
        }
        try {
            return Integer.parseInt(e.getAsString().replaceAll("\\D+", ""));
        } catch (Exception ex) {
            return 0;
        }
    }
}
