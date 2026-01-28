package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * AI-powered test failure analyzer that uses GitHub Models API (OpenAI)
 * to analyze test failures and provide actionable suggestions.
 */
public class AIFailureAnalyzer {

    private static final Logger log = LogManager.getLogger(AIFailureAnalyzer.class);
    private static final String GITHUB_MODELS_API_URL = "https://models.inference.ai.azure.com/chat/completions";

    private final String githubToken;
    private final String aiModel;
    private final double temperature;
    private final boolean enabled;

    private static AIFailureAnalyzer instance;

    private AIFailureAnalyzer() {
        Properties props = loadConfig();
        this.enabled = true;
        this.githubToken = System.getenv("GITHUB_TOKEN");
        this.aiModel = "gpt-4o";
        this.temperature = 0.2;

        if (githubToken == null || githubToken.isEmpty()) {
            log.warn("AI Failure Analysis is enabled but GitHub token is not configured");
        }
    }

    public static synchronized AIFailureAnalyzer getInstance() {
        if (instance == null) {
            instance = new AIFailureAnalyzer();
        }
        return instance;
    }

    private Properties loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (Exception e) {
            log.error("Failed to load config for AI Failure Analyzer: " + e.getMessage());
        }
        return props;
    }

    /**
     * Analyzes a test failure and returns AI-generated suggestions.
     */
    public String analyzeFailure(String testName, String errorMessage, String stackTrace) {
        return analyzeFailure(testName, errorMessage, stackTrace, null);
    }

    /**
     * Analyzes a test failure with test parameters and returns AI-generated suggestions.
     */
    public String analyzeFailure(String testName, String errorMessage, String stackTrace, Object[] testParameters) {
        if (!enabled) {
            log.debug("AI Failure Analysis is disabled");
            return null;
        }

        if (githubToken == null || githubToken.isEmpty()) {
            log.warn("Cannot perform AI analysis: GitHub token not configured");
            return null;
        }

        try {
            String prompt = buildAnalysisPrompt(testName, errorMessage, stackTrace, testParameters);
            return callAIApi(prompt);
        } catch (Exception e) {
            log.error("AI Failure Analysis failed: " + e.getMessage(), e);
            return null;
        }
    }

    private String buildAnalysisPrompt(String testName, String errorMessage, String stackTrace, Object[] testParameters) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("You are a test automation expert. Analyze this failed Selenium/TestNG test.\n\n");
        promptBuilder.append("**Test Name:** ").append(testName).append("\n\n");

        // 🔹 Always include ALL parameters in a clear, explicit format
        promptBuilder.append("**Test Parameters / Arguments (all):**\n");
        promptBuilder.append(formatParameters(testParameters)).append("\n");

        // 🔹 Also include as JSON for unambiguous reference
        promptBuilder.append("**Parameters (JSON):**\n");
        promptBuilder.append(parametersAsJson(testParameters)).append("\n\n");

        promptBuilder.append("**Error Message:**\n").append(errorMessage).append("\n\n");
        promptBuilder.append("**Stack Trace:**\n").append(truncateStackTrace(stackTrace, 1500)).append("\n\n");

        // 🔹 Strong instruction to reference ALL parameters in the output
        promptBuilder.append("""
            Provide a CONCISE analysis in exactly 10-12 lines covering:
            1. ROOT CAUSE: What caused this failure (2-3 lines) 
            2. SOLUTION: How to fix it (3-4 lines) 
            3. PREVENTION: How to avoid this in future (2-3 lines)

            Requirements:
            - No need of including parameters.
            - Be specific and actionable. No lengthy explanations.
            """);

        return promptBuilder.toString();
    }

    private String truncateStackTrace(String stackTrace, int maxLength) {
        if (stackTrace == null) return "No stack trace available";
        if (stackTrace.length() <= maxLength) return stackTrace;
        return stackTrace.substring(0, maxLength) + "\n... [truncated]";
    }

    private String callAIApi(String prompt) throws Exception {
        URL url = URI.create(GITHUB_MODELS_API_URL).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + githubToken);
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(60000);

            String requestBody = buildRequestBody(prompt);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream is = conn.getInputStream()) {
                    String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    return parseAIResponse(response);
                }
            } else {
                try (InputStream es = conn.getErrorStream()) {
                    String error = es != null ? new String(es.readAllBytes(), StandardCharsets.UTF_8) : "Unknown error";
                    log.error("AI API returned error code " + responseCode + ": " + error);
                    return null;
                }
            }
        } finally {
            conn.disconnect();
        }
    }

    private String buildRequestBody(String prompt) {
        String escapedPrompt = prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        return String.format("""
            {
                "model": "%s",
                "messages": [
                    {
                        "role": "system",
                        "content": "You are a helpful test automation expert who analyzes test failures and provides clear, actionable insights."
                    },
                    {
                        "role": "user",
                        "content": "%s"
                    }
                ],
                "temperature": %s,
                "max_tokens": 1000
            }
            """, aiModel, escapedPrompt, temperature);
    }

    private String parseAIResponse(String jsonResponse) {
        try {
            int contentStart = jsonResponse.indexOf("\"content\":");
            if (contentStart == -1) {
                log.warn("Could not find content in AI response");
                return null;
            }

            int valueStart = jsonResponse.indexOf("\"", contentStart + 10) + 1;
            if (valueStart == 0) return null;

            int valueEnd = valueStart;
            while (valueEnd < jsonResponse.length()) {
                int nextQuote = jsonResponse.indexOf("\"", valueEnd);
                if (nextQuote == -1) break;

                int backslashCount = 0;
                int checkPos = nextQuote - 1;
                while (checkPos >= valueStart && jsonResponse.charAt(checkPos) == '\\') {
                    backslashCount++;
                    checkPos--;
                }

                if (backslashCount % 2 == 0) {
                    valueEnd = nextQuote;
                    break;
                }
                valueEnd = nextQuote + 1;
            }

            String content = jsonResponse.substring(valueStart, valueEnd);

            return content
                    .replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\t", "\t")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");

        } catch (Exception e) {
            log.error("Failed to parse AI response: " + e.getMessage());
            return null;
        }
    }

    /**
     * Formats the AI analysis for HTML display in Extent Report (safe, escaped).
     */
    public String formatForExtentReport(String analysis) {
        if (analysis == null || analysis.isEmpty()) {
            return null;
        }

        String escaped = escapeHtml(analysis);
        return "<div style='background-color:#1a1a2e; padding:15px; border-left:4px solid #4da6ff; margin:10px 0;'>"
                + "<b style='color:#4da6ff;'>🤖 AI Failure Analysis</b><br/><br/>"
                + "<pre style='white-space:pre-wrap; color:#e6e6e6; font-family:Consolas, Menlo, Monaco, monospace; margin:0;'>"
                + escaped
                + "</pre>"
                + "</div>";
    }

    public String formatForAllureReport(String analysis) {
        if (analysis == null || analysis.isEmpty()) {
            return null;
        }
        return "🤖 AI FAILURE ANALYSIS\n"
                + "========================\n\n"
                + analysis;
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String formatParameters(Object[] testParameters) {
        if (testParameters == null || testParameters.length == 0) return "No parameters";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < testParameters.length; i++) {
            Object p = testParameters[i];
            String type = (p == null) ? "null" : p.getClass().getSimpleName();
            String value = (p == null) ? "null" : String.valueOf(p);
            sb.append(String.format("  - Param %d (%s): %s%n", i + 1, type, value));
        }
        return sb.toString();
    }

    private String parametersAsJson(Object[] testParameters) {
        if (testParameters == null || testParameters.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < testParameters.length; i++) {
            Object p = testParameters[i];
            String type = (p == null) ? "null" : p.getClass().getSimpleName();
            String value = (p == null) ? "null" : String.valueOf(p)
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"");
            sb.append("{")
                    .append("\"index\":").append(i + 1).append(",")
                    .append("\"type\":\"").append(type).append("\",")
                    .append("\"value\":\"").append(value).append("\"")
                    .append("}");
            if (i < testParameters.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean isEnabled() {
        return enabled;
    }
}
