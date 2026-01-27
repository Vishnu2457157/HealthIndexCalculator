
package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.CalculatorForm;
import pages.ResultPage;
import pages.TopNavigation;

import java.time.Duration;

import static org.testng.Assert.assertTrue;


public class ResultsPageUiTest extends BaseTest {




    @BeforeClass
    public void setUp() {
        log.info("Initializing page objects: TopNavigation, CalculatorForm, ResultPage");

        tn = new TopNavigation(driver);
        cf = new CalculatorForm(driver);
        res = new ResultPage(driver);

        log.info("Page objects initialized successfully");
    }

    @Test(priority = 1, dataProvider = "resultPageCheck")
    void resultPageCheck(String age, String pulse, String bp) throws InterruptedException {
        log.info("=== Starting 'resultPageCheck' test ===");

        log.info(String.format("Input data -> Age: %s, Pulse: %s, BP: %s", age, pulse, bp));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        SoftAssert sa = new SoftAssert();

        log.info("Entering age");
        cf.enterAge(age);

        log.info("Entering pulse");
        cf.enterPulse(pulse);

        log.info("Entering blood pressure");
        cf.enterBloodPressure(bp);

        log.info("Clicking Calculate");
        cf.clickCalculate();

        log.info("Locating feedback text area");
        WebElement textArea = driver.findElement(By.id("feedback"));

        log.info("Computing scroll metrics: scrollHeight vs clientHeight");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long scrollHeight = (Long) js.executeScript("return arguments[0].scrollHeight;", textArea);
        Long clientHeight = (Long) js.executeScript("return arguments[0].clientHeight;", textArea);

        log.info("scrollHeight: " + scrollHeight);
        log.info("clientHeight: " + clientHeight);

        boolean scrollable = scrollHeight > clientHeight;
        log.info("Is feedback textarea scrollable? " + scrollable);

        if (scrollable) {
            log.info("PASS: The result text box is scrollable");
            sa.assertTrue(scrollable, "The result text box is scrollable");
        } else {
            log.info("FAIL: The result text box is not scrollable");
            sa.fail("The result text box is not scrollable");
        }

        log.info("Checking CSS 'resize' property");
        String resizeCSS = textArea.getCssValue("resize");
        log.info("resize CSS value: " + resizeCSS);

        boolean resizable = !"none".equalsIgnoreCase(resizeCSS);
        log.info("Is feedback textarea resizable? " + resizable);

        if (resizable) {
            log.info("PASS: The result textbox is resizable");
            sa.assertTrue(resizable, "Theresult textbox is resizable");
        } else {
            log.info("FAIL: The result textbox is not resizable");
            sa.fail("The result textbox is not resizable");
        }

        log.info("Asserting all soft assertions");
        sa.assertAll();

        log.info("=== Completed 'resultPageCheck' test ===");
    }
    @Test(priority = 2)
    public void isLogoPresent() {
        log.info("=== Starting 'isLogoPresent' test ===");
        boolean present = res.isLogoPresent();
        log.info("Logo present? " + present);
        assertTrue(present, "Logo should be visible");
        log.info("PASS: Logo is visible");
        log.info("=== Completed 'isLogoPresent' test ===");
    }
    @Test(priority = 3)
    public void isLogoTextPresent() {
        log.info("=== Starting 'isLogoTextPresent' test ===");
        boolean present = res.isLogoTextPresent();
        log.info("Logo text present? " + present);
        assertTrue(present, "Logo text should be visible");
        log.info("PASS: Logo text is visible");
        log.info("=== Completed 'isLogoTextPresent' test ===");
    }
    @Test(priority = 4)
    public void isResultBoxPresent() {
        log.info("=== Starting 'isResultBoxPresent' test ===");
        boolean present = res.isResultBoxPresent();
        log.info("Result box present? " + present);
        assertTrue(present, "result box should be visible");
        log.info("PASS: Result box is visible");
        log.info("=== Completed 'isResultBoxPresent' test ===");
    }
    @Test(priority = 5)
    public void isHeadingPresent() {
        log.info("=== Starting 'isHeadingPresent' test ===");
        boolean present = res.isHeadingPresent();
        log.info("Heading present? " + present);
        assertTrue(present, "Heading should be visible");
        log.info("PASS: Heading is visible");
        log.info("=== Completed 'isHeadingPresent' test ===");
    }
    @Test(priority = 6)
    public void isResultDisplayed() {
        log.info("=== Starting 'isResultDisplayed' test ===");
        boolean displayed = res.isResultDisplayed();
        log.info("Result displayed? " + displayed);
        assertTrue(displayed, "result should be displayed");
        log.info("PASS: Result is displayed");
        log.info("=== Completed 'isResultDisplayed' test ===");
    }
    @Test(priority = 7)
    public void isFeedbackBoxdisplayed() {
        log.info("=== Starting 'isFeedbackBoxdisplayed' test ===");
        boolean present = res.isFeedbackBoxPresent();
        log.info("Feedback box present? " + present);
        assertTrue(present, "feedback box must be present");
        log.info("PASS: Feedback box is present");
        log.info("=== Completed 'isFeedbackBoxdisplayed' test ===");
    }
    @DataProvider(name = "resultPageCheck")
    public Object[][] sendData() {
        log.info("Providing data for 'resultPageCheck'");

        Object data[][] = {
                {"20", "67", "120/80"}
        };
        log.info("Data provided: {} row(s)",data.length);
        return data;
    }

}

