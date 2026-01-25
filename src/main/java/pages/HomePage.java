
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;


import java.time.Duration;

public class HomePage {
    //private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    WebDriver driver;
    TopNavigation topNav;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.topNav = new TopNavigation(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
     //   log.debug("HomePage initialized with driver {}", driver);
    }

    @FindBy(xpath="//div[@class='image']/img")
    WebElement image;

    @FindBy(css=".calc")
    WebElement calculatorForm;

    public boolean isLeftImagePresent() {
        try {
         //   log.info("Waiting for left image to be visible...");
            wait.until(ExpectedConditions.visibilityOf(image));
            boolean present = image.isDisplayed();
            //log.info("Left image present: {}", present);
            return present;
        } catch (Exception e) {
           // log.error("Left image not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isCalculatorPresent() {
        try {
            //log.info("Waiting for calculator form to be visible...");
            wait.until(ExpectedConditions.visibilityOf(calculatorForm));
            boolean present = calculatorForm.isDisplayed();
            //log.info("Calculator form present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Calculator form not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTopNavPresent() {
        boolean present = topNav.isNavBarPresent();
        //log.info("Top navigation present: {}", present);
        return present;
    }

    public void goToHeartPulse() {
        //log.info("Navigating to Heart Pulse from Home page...");
        topNav.goToHeartPulse();
       // log.info("Navigation to Heart Pulse complete.");
    }

    public void goToBloodPressure() {
        //log.info("Navigating to Blood Pressure from Home page...");
        topNav.goToBloodPressure();
        //log.info("Navigation to Blood Pressure complete.");
    }

    public void goToAgeFactor() {
       // log.info("Navigating to Age Factor from Home page...");
        topNav.goToAgeFactor();
        //log.info("Navigation to Age Factor complete.");
    }

    public void goToTeamDetails() {
        //log.info("Navigating to Team Details from Home page...");
        topNav.goToTeamDetails();
       // log.info("Navigation to Team Details complete.");
    }

    public void goHomeViaLogoText() {
        //log.info("Navigating to Home via logo text...");
        topNav.clickLogoText();
        //log.info("Navigation via logo text complete.");
    }
}

