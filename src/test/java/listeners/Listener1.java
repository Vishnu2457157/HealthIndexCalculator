package listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.AIFailureAnalyzer;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class Listener1 implements ITestListener {
    private static final Logger log = LoggerFactory.getLogger(Listener1.class);

    private ExtentReports extent;
    // One ExtentTest per executing thread (works for parallel runs)
    private final ConcurrentHashMap<Long, ExtentTest> testMap = new ConcurrentHashMap<>();
    String reportName;

    @Override
    public void onStart(ITestContext context) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportName = "Test-Report" + timeStamp + ".html";
        String reportPath = System.getProperty("user.dir") + "/reports/" + reportName;
        log.info("Initializing ExtentReports. Report path: {}", reportPath);

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setDocumentTitle("Test Report");
        spark.config().setReportName("Health Index Calculator Test Report");
        spark.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));

        // Optional: include browser if provided via context params
        String browser = context.getCurrentXmlTest() != null
                ? context.getCurrentXmlTest().getParameter("browser")
                : null;
        if (browser != null) {
            extent.setSystemInfo("Browser", browser);
            log.info("System info set: Browser={}", browser);
        }

        // Add AI analyzer info
        AIFailureAnalyzer ai = AIFailureAnalyzer.getInstance();
        extent.setSystemInfo("AI Failure Analysis", ai.isEnabled() ? "Enabled" : "Disabled");
        extent.setSystemInfo("AI Model", "gpt-4o");

        log.info("ExtentReports initialization complete");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.info("onTestStart -> {}", testName);

        ExtentTest test = extent.createTest(testName)
                .assignCategory(result.getMethod().getRealClass().getSimpleName());
        testMap.put(Thread.currentThread().getId(), test);

        // Optional: log data provider parameters
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            String paramStr = Arrays.toString(params);
            log.info("Parameters for {}: {}", testName, paramStr);
            test.info("Parameters: " + paramStr);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String name = result.getName();
        log.info("onTestSuccess -> {}", name);

        test().pass("PASSED: " + name);
        String prePath = asString(result.getAttribute("preSubmitScreenshotPath"));
        if (prePath != null) {
            log.info("Deleting pre-submit screenshot for passed test: {}", prePath);
        }
        safeDelete(prePath);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String name = result.getName();
        log.error("onTestFailure -> {}", name, result.getThrowable());

        test().fail("FAILED: " + name);
        if (result.getThrowable() != null) {
            test().fail(result.getThrowable());
        }

        // Attach screenshots (if provided by the test)
        String prePath = asString(result.getAttribute("preSubmitScreenshotPath"));
        String failPath = asString(result.getAttribute("failureScreenshotPath"));

        if (prePath != null) {
            log.info("Attaching pre-submit screenshot: {}", prePath);
            test().log(Status.INFO, "Pre-submit screenshot: " + prePath);
            try {
                test().addScreenCaptureFromPath(prePath);
            } catch (Exception e) {
                log.warn("Failed to attach pre-submit screenshot: {}", e.getMessage());
                test().log(Status.WARNING, "Failed to attach pre-submit screenshot: " + e.getMessage());
            }
        } else {
            log.info("No pre-submit screenshot path provided by the test.");
            test().log(Status.INFO, "No pre-submit screenshot path was provided by the test.");
        }

        if (failPath != null) {
            log.info("Attaching failure screenshot: {}", failPath);
            test().log(Status.INFO, "Failure screenshot: " + failPath);
            try {
                test().addScreenCaptureFromPath(failPath);
            } catch (Exception e) {
                log.warn("Failed to attach failure screenshot: {}", e.getMessage());
                test().log(Status.WARNING, "Failed to attach failure screenshot: " + e.getMessage());
            }
        } else {
            log.info("No failure screenshot path provided by the test.");
            test().log(Status.INFO, "No failure screenshot path was provided by the test.");
        }

        // ==== AI Failure Analysis ====
        try {
            AIFailureAnalyzer ai = AIFailureAnalyzer.getInstance();

            final String testName = result.getMethod() != null ? result.getMethod().getMethodName() : name;
            final Throwable t = result.getThrowable();
            final String errorMessage = t != null && t.getMessage() != null ? t.getMessage() : "No error message";
            final String stackTrace = getStackTraceAsString(t);
            final Object[] params = result.getParameters();

            // Prepend a human-rendered parameters block before AI output (guaranteed visibility of ALL params)
            String paramsHtml = renderParametersBlock(params);

            String analysis = ai.analyzeFailure(testName, errorMessage, stackTrace, params);
            if (analysis != null && !analysis.isEmpty()) {
                String html = ai.formatForExtentReport(analysis);
                if (html != null) {
                    test().info(paramsHtml + html); // parameters + AI analysis
                    log.info("AI Failure Analysis attached to report for test: {}", testName);
                } else {
                    test().info(paramsHtml);
                }
            } else {
                log.warn("AI Failure Analysis returned no content for test: {}", testName);
                test().info(paramsHtml);
                test().log(Status.INFO, "🤖 AI Failure Analysis: Not available (token missing or service error).");
            }
        } catch (Exception e) {
            log.warn("AI Failure Analysis step failed: {}", e.getMessage());
            test().log(Status.WARNING, "AI Failure Analysis step failed: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String name = result.getName();
        log.warn("onTestSkipped -> {}", name, result.getThrowable());

        test().skip("SKIPPED: " + name);
        if (result.getThrowable() != null) {
            test().skip(result.getThrowable());
        }

        String prePath = asString(result.getAttribute("preSubmitScreenshotPath"));
        if (prePath != null) {
            log.info("Deleting pre-submit screenshot for skipped test: {}", prePath);
        }
        safeDelete(prePath);
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Flushing ExtentReports...");
        try {
            if (extent != null) {
                extent.flush();
                log.info("ExtentReports flushed successfully");
            } else {
                log.warn("ExtentReports was null onFinish");
            }
        } finally {
            testMap.clear();
            log.info("Cleared testMap. Listener finished.");
        }
    }

    /** Helper: get current thread's ExtentTest */
    private ExtentTest test() {
        ExtentTest t = testMap.get(Thread.currentThread().getId());
        if (t == null) {
            log.warn("ExtentTest was null for thread {}, creating fallback test",
                    Thread.currentThread().getId());
            t = extent.createTest("UnknownTest-" + Thread.currentThread().getId());
            testMap.put(Thread.currentThread().getId(), t);
        }
        return t;
    }

    /** Convert an attribute value to String, or return null if not a String. */
    private String asString(Object o) {
        return (o instanceof String) ? (String) o : null;
    }

    /** Delete file quietly (used so pre-submit screenshots exist only for failed tests). */
    private void safeDelete(String path) {
        if (path == null) return;
        try {
            File f = new File(path);
            if (f.exists()) {
                boolean deleted = f.delete();
                log.debug("Deleted file {} -> {}", path, deleted);
            }
        } catch (Exception e) {
            // Intentionally ignore to avoid failing the listener due to FS issues
            log.warn("safeDelete encountered exception for {}: {}", path, e.getMessage());
        }
    }

    /** Convert Throwable stack trace to String (no external libs). */
    private String getStackTraceAsString(Throwable t) {
        if (t == null) return "No stack trace available";
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        } catch (Exception e) {
            return "Failed to capture stack trace: " + e.getMessage();
        }
    }

    /** Render all parameters in a styled HTML block (ensures full visibility in the report). */
    private String renderParametersBlock(Object[] params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='background:#0f1624;padding:10px;border-left:4px solid #888;margin:10px 0;'>")
                .append("<b style='color:#bbb;'>Test Parameters (echo):</b><br/>")
                .append("<pre style='white-space:pre-wrap;color:#e6e6e6;margin:0;'>");

        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                Object p = params[i];
                String type = (p == null) ? "null" : p.getClass().getSimpleName();
                String value = (p == null) ? "null" : String.valueOf(p);
                if(i==0){
                    sb.append("Age: ");
                }
                else if(i==1){
                    sb.append("Pulse Rate: ");
                }
                else if(i==2){
                    sb.append("Blood Pressure: ");
                }
                else{
                    sb.append("Test-Type: ");
                }
                sb.append(escapeForHtml(value)).append("\n");
            }
        } else {
            sb.append("No parameters");
        }

        sb.append("</pre></div>");
        return sb.toString();
    }

    /** Escape HTML for safe rendering in Extent step logs. */
    private String escapeForHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
