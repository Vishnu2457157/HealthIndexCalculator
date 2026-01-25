package hooks;



import ai.AIService;
import ai.TestDataMapper;
import data.ExcelWriter;
import io.restassured.RestAssured;
import models.HealthDataRow;
import org.testng.annotations.BeforeSuite;

import java.nio.file.Path;
import java.util.List;

public class SuiteHooks {

    private static final String OUT_XLSX = "src/test/resources/testdata/AI_TestData.xlsx";

    @BeforeSuite(alwaysRun = true)
    public void generateAiTestData() throws Exception {
        // Optional toggle: only generate when -DuseAIData=true
        String flag = System.getProperty("useAIData", "true");
        if (!Boolean.parseBoolean(flag)) {
            System.out.println("AI data generation skipped (useAIData=false).");
            return;
        }
        String prompt =
                "Generate 50 diverse test rows for a Health Index Calculator. " +
                        "Return ONLY a valid JSON array, no comments, no markdown fences. " +
                        "Each object must have numeric fields: " +
                        "age (0..120), pulseRate (30..220), bloodPressure (40..250). " +
                        "Include normal, boundary, and a few extreme values. Example: " +
                        "[{\"age\":25,\"pulseRate\":72,\"bloodPressure\":120}]";

        String apiJson = AIService.generateResponse(prompt);
        List<HealthDataRow> rows = TestDataMapper.mapFromOpenAIResponse(apiJson);
        ExcelWriter.writeHealthRows(rows, OUT_XLSX);

        System.out.println("AI test data written to: " + Path.of(OUT_XLSX).toAbsolutePath());
    }
}

