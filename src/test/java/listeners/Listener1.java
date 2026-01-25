
package listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
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
        String timeStamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportName="Test-Report"+timeStamp+".html";
        String reportPath = System.getProperty("user.dir") + "/reports/"+reportName;
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

        // Requirement: screenshots should be saved only if the test failed.
        // So delete the pre-submit screenshot (if any) for passed tests.
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

        // Do NOT take screenshots here (per requirement).
        // Attach the screenshots captured in the test class.
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
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String name = result.getName();
        log.warn("onTestSkipped -> {}", name, result.getThrowable());

        test().skip("SKIPPED: " + name);
        if (result.getThrowable() != null) {
            test().skip(result.getThrowable());
        }

        // Optional: cleanup pre-submit file even for skipped tests
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
}
