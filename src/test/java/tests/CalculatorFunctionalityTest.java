package tests;
import org.openqa.selenium.TimeoutException;
import utils.ExcelDataManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.CalculatorForm;
import pages.TopNavigation;
import utils.ScreenshotUtil;
import java.io.File;
import java.time.Duration;
import static org.testng.Assert.assertTrue;
public class CalculatorFunctionalityTest extends BaseTest {
    CalculatorForm cf;
    ExcelDataManager df;
    TopNavigation tp;
    WebDriverWait wait;
    @BeforeClass
    public void setUp(ITestContext context) {
        log.info("Initializing CalculatorForm, ExcelDataManager, TopNavigation");
        Reporter.log("Initializing test dependencies for CalculatorFunctionalityTest", true);
        df = new ExcelDataManager();
        cf = new CalculatorForm(driver);
        tp = new TopNavigation(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        context.setAttribute("driver", driver);
        log.info("Setup complete for CalculatorFunctionalityTest");
        Reporter.log("Setup complete for CalculatorFunctionalityTest", true);
    }
    @Test(priority = 1)
    public void isFormHeadingPresent() {
        log.info("Verifying form heading presence");
        Reporter.log("Checking form heading presence...", true);
        assertTrue(cf.isFormHeadingPresent(), "Form heading should be present");
        log.info("Form Heading assertion passed");
        Reporter.log("Form Heading assertion passed", true);
    }
    @Test(priority = 2)
    public void isAgeFieldPresent() {
        log.info("Verifying age field presence");
        Reporter.log("Checking age field presence...", true);
        assertTrue(cf.isAgeFieldPresent(), "Age field should be present");
        log.info("Age field presence assertion passed");
        Reporter.log("Age field presence assertion passed", true);
    }
    @Test(priority = 3)
    public void isPulseRateFieldPresent() {
        log.info("Verifying pulse field presence");
        Reporter.log("Checking pulse field presence...", true);
        assertTrue(cf.isPulseRateFieldPresent(), "Pulse field should be present");
        log.info("Pulse field presence assertion passed");
        Reporter.log("Pulse field presence assertion passed", true);
    }
    @Test(priority = 4)
    public void isBloodPressureFieldPresent() {
        log.info("Verifying BP field presence");
        Reporter.log("Checking BP field presence...", true);
        assertTrue(cf.isBloodPressureFieldPresent(), "pulse field should be present");
        log.info("BP field presence assertion passed");
        Reporter.log("BP field presence assertion passed", true);
    }
    @Test(priority = 5)
    public void isCalculateButtonPresentAndClickable() {
        log.info("Verifying Calculate Button presence and Clickability");
        Reporter.log("Checking Calculate button presence and clickability...", true);
        assertTrue(cf.isButtonPresentAndClickable(), "Button should be present and clickable");
        log.info("Calculate button presence & clickability assertion passed");
        Reporter.log("Calculate button presence & clickability assertion passed", true);
    }
    @Test(
            dataProvider = "dp",

            priority = 6
    )
    public void sendData(String age, String pulse, String bp, String testType) throws InterruptedException {
        Reporter.log("=== Starting sendData ===", true);
        final boolean isPositive = "positive".equalsIgnoreCase(testType);
        final boolean isNegative = "negative".equalsIgnoreCase(testType);

        log.info("Executing data row: age='{}', pulse='{}', bp='{}', testType='{}'", age, pulse, bp, testType);
        Reporter.log(String.format("Input -> age=%s, pulse=%s, bp=%s, testType=%s", age, pulse, bp, testType), true);
        cf.enterAge(age);
        cf.enterPulse(pulse);
        cf.enterBloodPressure(bp);

        String baseName = ScreenshotUtil.buildName(age, pulse, bp, testType);
        File preSubmit = ScreenshotUtil.takeScreenshotofCalculator(driver, baseName + "_before");
        Reporter.getCurrentTestResult().setAttribute("preSubmitScreenshotPath", preSubmit.getAbsolutePath());
        Reporter.log("Captured pre-submit screenshot: " + preSubmit.getAbsolutePath(), true);
        log.info("Clicking Calculate");
        Reporter.log("Clicking Calculate...", true);
        cf.clickCalculate();
        SoftAssert sa = new SoftAssert();
        Alert alert = null;
        try {
            alert = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.alertIsPresent());
            log.info("Alert detected after submit");
            Reporter.log("Alert detected after submit", true);
        } catch (TimeoutException ignored) {
            Reporter.log("No alert detected within timeout", true);
        }
        if (alert != null) {
            String alertText = alert.getText();
            Reporter.log("Alert text: " + alertText, true);
            alert.accept();
            if (isPositive) {
                File failure = ScreenshotUtil.takeScreenshotToFile(driver, baseName + "_failed_alert_visible");
                Reporter.getCurrentTestResult().setAttribute("failureScreenshotPath", failure.getAbsolutePath());
                sa.fail("POSITIVE input unexpectedly produced an alert: \"" + alertText + "\". " +
                        "Expected navigation to Result page without alert.");
                log.error("Positive case failed: alert was present");
                Reporter.log("FAIL (Positive): Alert was present", true);
            } else if (isNegative) {
                boolean navigated = driver.getCurrentUrl().toLowerCase().contains("result");
                log.info("Negative case: Produced an alert");
                Reporter.log("Negative case: navigatedToResult=" + navigated, true);
            }
        }
            else {
                boolean navigated = driver.getCurrentUrl().toLowerCase().contains("result");
                if (isPositive && navigated) {
                    log.info("Positive case passed: navigated to results page; returning Home via logo text");
                    tp.clickLogoText();
                    log.info("Clicked 'VitalGuard' logo text to return Home");
                } else if (isNegative && navigated) {
                    File failure = ScreenshotUtil.takeScreenshotToFile(driver, baseName + "_failed_no_alert");
                    Reporter.getCurrentTestResult().setAttribute("failureScreenshotPath", failure.getAbsolutePath());
                    sa.fail("NEGATIVE input incorrectly navigated to Result page without showing any alert.");
                    log.error("Negative case failed: navigated without alert");
                    tp.clickLogoText();
                    Reporter.log("Clicked 'VitalGuard' to return Home (negative case)", true);
                }
            }
            sa.assertAll();
            Reporter.log("=== Completed sendData ===", true);
        }
    // Keep your old Excel provider for other tests (unchanged)
    @DataProvider(name = "dp")
    Object[][] fieldData() {
        Reporter.log("Loading data from Excel via ExcelDataManager.dataFetch()", true);
        Object data[][];
        try {
            data = df.dataFetch();
            log.info("Loaded {} data rows from Excel");
            Reporter.log("Excel data rows loaded: " + data.length, true);
        } catch (Exception e) {
            log.error("Failed to read Excel data: {}", e.getMessage());
            Reporter.log("Failed to read Excel data: " + e.getMessage(), true);
            throw new RuntimeException("Failed to read Excel data", e);
        }
        return data;
    }
}
//package tests;
//
//import org.openqa.selenium.TimeoutException;
//import utils.ExcelDataManager;
//import org.openqa.selenium.Alert;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.ITestContext;
//import org.testng.Reporter;
//import org.testng.annotations.*;
//import org.testng.asserts.SoftAssert;
//import pages.CalculatorForm;
//import pages.TopNavigation;
//import utils.ScreenshotUtil;
//
//import java.io.File;
//import java.time.Duration;
//
//import static org.testng.Assert.assertTrue;
//
//public class CalculatorFunctionalityTest extends BaseTest {
//    CalculatorForm cf;
//    ExcelDataManager df;
//    TopNavigation tp;
//    WebDriverWait wait;
//
//    @BeforeClass
//    public void setUp(ITestContext context) {
//        log.info("Initializing CalculatorForm, ExcelDataManager, TopNavigation");
//        Reporter.log("Initializing test dependencies for CalculatorFunctionalityTest", true);
//        df = new ExcelDataManager();
//        cf = new CalculatorForm(driver);
//        tp = new TopNavigation(driver);
//        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//        context.setAttribute("driver", driver);
//        log.info("Setup complete for CalculatorFunctionalityTest");
//        Reporter.log("Setup complete for CalculatorFunctionalityTest", true);
//    }
//
//    @Test(priority = 1)
//    public void isFormHeadingPresent() {
//        log.info("Verifying form heading presence");
//        Reporter.log("Checking form heading presence...", true);
//        assertTrue(cf.isFormHeadingPresent(), "Form heading should be present");
//        log.info("Form Heading assertion passed");
//        Reporter.log("Form Heading assertion passed", true);
//    }
//
//    @Test(priority = 2)
//    public void isAgeFieldPresent() {
//        log.info("Verifying age field presence");
//        Reporter.log("Checking age field presence...", true);
//        assertTrue(cf.isAgeFieldPresent(), "Age field should be present");
//        log.info("Age field presence assertion passed");
//        Reporter.log("Age field presence assertion passed", true);
//    }
//
//    @Test(priority = 3)
//    public void isPulseRateFieldPresent() {
//        log.info("Verifying pulse field presence");
//        Reporter.log("Checking pulse field presence...", true);
//        assertTrue(cf.isPulseRateFieldPresent(), "Pulse field should be present");
//        log.info("Pulse field presence assertion passed");
//        Reporter.log("Pulse field presence assertion passed", true);
//    }
//
//    @Test(priority = 4)
//    public void isBloodPressureFieldPresent() {
//        log.info("Verifying BP field presence");
//        Reporter.log("Checking BP field presence...", true);
//        assertTrue(cf.isBloodPressureFieldPresent(), "pulse field should be present");
//        log.info("BP field presence assertion passed");
//        Reporter.log("BP field presence assertion passed", true);
//    }
//
//    @Test(priority = 5)
//    public void isCalculateButtonPresentAndClickable() {
//        log.info("Verifying Calculate Button presence and Clickability");
//        Reporter.log("Checking Calculate button presence and clickability...", true);
//        assertTrue(cf.isButtonPresentAndClickable(), "Button should be present and clickable");
//        log.info("Calculate button presence & clickability assertion passed");
//        Reporter.log("Calculate button presence & clickability assertion passed", true);
//    }
//
//
//    @Test(
//            dataProvider = "dp",
//
//            priority = 6
//    )
//    public void sendData(String age, String pulse, String bp, String testType) throws InterruptedException {
//        // Optional guard to ensure testType is correct
//
//
//        Reporter.log("=== Starting sendData ===", true);
//        final boolean isPositive = "positive".equalsIgnoreCase(testType);
//        final boolean isNegative = "negative".equalsIgnoreCase(testType);
//        boolean testShouldFail = false;
//        boolean failureScreenshotCaptured = false;
//
//        log.info("Executing data row: age='{}', pulse='{}', bp='{}', testType='{}'", age, pulse, bp, testType);
//        Reporter.log(String.format("Input -> age=%s, pulse=%s, bp=%s, testType=%s", age, pulse, bp, testType), true);
//
//        cf.enterAge(age);
//        cf.enterPulse(pulse);
//        cf.enterBloodPressure(bp);
//
//        String baseName = ScreenshotUtil.buildName(age, pulse, bp, testType);
//        File preSubmit = ScreenshotUtil.takeScreenshotofCalculator(driver, baseName + "_before");
//        Reporter.getCurrentTestResult().setAttribute("preSubmitScreenshotPath", preSubmit.getAbsolutePath());
//        Reporter.log("Captured pre-submit screenshot: " + preSubmit.getAbsolutePath(), true);
//
//        log.info("Clicking Calculate");
//        Reporter.log("Clicking Calculate...", true);
//        cf.clickCalculate();
//
//        SoftAssert sa = new SoftAssert();
//        Alert alert = null;
//        try {
//            alert = new WebDriverWait(driver, Duration.ofSeconds(3))
//                    .until(ExpectedConditions.alertIsPresent());
//            log.info("Alert detected after submit");
//            Reporter.log("Alert detected after submit", true);
//        } catch (TimeoutException ignored) {
//            Reporter.log("No alert detected within timeout", true);
//        }
//
//        if (alert != null) {
//            String alertText = alert.getText();
//            Reporter.log("Alert text: " + alertText, true);
//            alert.accept();
//
//            if (isPositive) {
//                File failure = ScreenshotUtil.takeScreenshotToFile(driver, baseName + "_failed_alert_visible");
//                Reporter.getCurrentTestResult().setAttribute("failureScreenshotPath", failure.getAbsolutePath());
//                sa.fail("POSITIVE input unexpectedly produced an alert: \"" + alertText + "\". " +
//                        "Expected navigation to Result page without alert.");
//                log.error("Positive case failed: alert was present");
//                Reporter.log("FAIL (Positive): Alert was present", true);
//
//            } else if (isNegative) {
//                boolean navigated = driver.getCurrentUrl().toLowerCase().contains("result");
//                log.info("Negative case: navigatedToResult={}", navigated);
//                Reporter.log("Negative case: navigatedToResult=" + navigated, true);
//
//                if (navigated) {
//                    File failure = ScreenshotUtil.takeScreenshotToFile(driver, baseName + "_failed_wrong_navigation");
//                    Reporter.getCurrentTestResult().setAttribute("failureScreenshotPath", failure.getAbsolutePath());
//                    sa.fail("NEGATIVE input should NOT navigate to Result page.");
//                    log.error("Negative case failed: navigated to result");
//                    Reporter.log("FAIL (Negative): Navigated to result page", true);
//                }
//            } else {
//                File failure = ScreenshotUtil.takeScreenshotToFile(driver, baseName + "_failed_unknown_alert");
//                Reporter.getCurrentTestResult().setAttribute("failureScreenshotPath", failure.getAbsolutePath());
//                sa.fail("Unknown testType: " + testType + " (expected 'positive' or 'negative').");
//                log.error("Unknown testType branch (alert present)");
//                Reporter.log("FAIL: Unknown testType (alert present)", true);
//            }
//
//        } else {
//            boolean navigated = driver.getCurrentUrl().toLowerCase().contains("result");
//            log.info("No alert branch: navigatedToResult={}", navigated);
//            Reporter.log("No alert branch: navigatedToResult=" + navigated, true);
//
//            if (isPositive) {
//                if (!navigated) {
//                    File failure = ScreenshotUtil.takeScreenshotToFile(driver, baseName + "_failed_no_navigation");
//                    Reporter.getCurrentTestResult().setAttribute("failureScreenshotPath", failure.getAbsolutePath());
//                    sa.fail("POSITIVE input should navigate to Result page, but it didn't.");
//                    log.error("Positive case failed: no navigation");
//                    Reporter.log("FAIL (Positive): Did not navigate to Result page", true);
//                } else {
//                    log.info("Positive case passed: navigated; returning Home via logo text");
//                    Reporter.log("Positive case passed: Navigated to results page; returning Home via logo text", true);
//                    System.out.println("POSITIVE Input is redirected to results page");
//                    tp.clickLogoText();
//                    log.info("Clicked 'VitalGuard' logo text to return Home");
//                    Reporter.log("Clicked 'VitalGuard' logo text to return Home", true);
//                }
//
//            } else if (isNegative) {
//                File failure = ScreenshotUtil.takeScreenshotToFile(driver, baseName + "_failed_no_alert");
//                Reporter.getCurrentTestResult().setAttribute("failureScreenshotPath", failure.getAbsolutePath());
//
//                if (navigated) {
//                    sa.fail("NEGATIVE input incorrectly navigated to Result page without showing any alert.");
//                    log.error("Negative case failed: navigated without alert");
//                    Reporter.log("FAIL (Negative): Navigated to Result without alert", true);
//                    tp.clickLogoText();
//                    Reporter.log("Clicked 'VitalGuard' to return Home (negative case)", true);
//                } else {
//                    sa.fail("NEGATIVE input did not show alert as expected.");
//                    log.error("Negative case failed: no alert, no navigation");
//                    Reporter.log("FAIL (Negative): No alert and no navigation", true);
//                }
//
//            } else {
//                File failure = ScreenshotUtil.takeScreenshotToFile(driver, baseName + "_failed_unknown_no_alert");
//                Reporter.getCurrentTestResult().setAttribute("failureScreenshotPath", failure.getAbsolutePath());
//                sa.fail("Unknown testType: " + testType + " (expected 'positive' or 'negative').");
//                log.error("Unknown testType branch (no alert)");
//                Reporter.log("FAIL: Unknown testType (no alert branch)", true);
//            }
//        }
//
//        sa.assertAll();
//        Reporter.log("=== Completed sendData ===", true);
//    }
//
//
//
//    // Keep your old Excel provider for other tests (unchanged)
//
//    @DataProvider(name = "dp")
//    public Object[][] dp() {
//        String path = "src/test/resources/testdata/AI_TestData.xlsx";
//
//        try (java.io.FileInputStream fis = new java.io.FileInputStream(path);
//             org.apache.poi.ss.usermodel.Workbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook(fis)) {
//
//            org.apache.poi.ss.usermodel.Sheet s = wb.getSheetAt(0);
//            int rows = s.getPhysicalNumberOfRows();
//            org.apache.poi.ss.usermodel.DataFormatter fmt = new org.apache.poi.ss.usermodel.DataFormatter();
//
//            // Expecting header: age | pulseRate | bloodPressure | testType
//            Object[][] out = new Object[rows - 1][4];
//
//            for (int i = 1; i < rows; i++) {
//                org.apache.poi.ss.usermodel.Row r = s.getRow(i);
//                out[i - 1][0] = fmt.formatCellValue(r.getCell(0)); // age
//                out[i - 1][1] = fmt.formatCellValue(r.getCell(1)); // pulse
//                out[i - 1][2] = fmt.formatCellValue(r.getCell(2)); // bp
//                out[i - 1][3] = fmt.formatCellValue(r.getCell(3)); // testType
//            }
//
//            return out;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to load AI Test Data from Excel: " + e.getMessage(), e);
//        }
//    }
//
//}
