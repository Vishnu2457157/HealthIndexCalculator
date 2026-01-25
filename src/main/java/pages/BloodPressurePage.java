
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;

public class BloodPressurePage {
   // private static final Logger log = LoggerFactory.getLogger(BloodPressurePage.class);

    private final WebDriverWait wait;
    private final TopNavigation topNav;

    public BloodPressurePage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        this.topNav = new TopNavigation(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        //log.debug("BloodPressurePage initialized with driver {}", driver);
    }

    @FindBy(xpath = "//div[@class='heading']/h2[contains(text(),'Blood Pressure')]")
    private WebElement heading;

    @FindBy(xpath = "//div[@class='agepara' and contains(normalize-space(),'Abnormal blood pressure and an aging population')]")
    private WebElement info;

    public boolean isHeadingPresent() {
        try {
           // log.info("Waiting for Blood Pressure heading to be visible...");
            wait.until(ExpectedConditions.visibilityOf(heading));
            boolean present = heading.isDisplayed();
            //log.info("Heading present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Heading not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isInfoPresent() {
        try {
            //log.info("Waiting for Blood Pressure informational block to be visible...");
            wait.until(ExpectedConditions.visibilityOf(info));
            boolean present = info.isDisplayed();
            //log.info("Info block present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Info block not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTopNavPresent() {
        boolean present = topNav.isNavBarPresent();
        //log.info("Top navigation present: {}", present);
        return present;
    }

    public void goToHome() {
        //log.info("Navigating to Home from Blood Pressure page...");
        topNav.goToHome();
        //log.info("Navigation to Home complete.");
    }

    public void goToHeartPulse() {
        //log.info("Navigating to Heart Pulse from Blood Pressure page...");
        topNav.goToHeartPulse();
        //log.info("Navigation to Heart Pulse complete.");
    }

    public void goToAgeFactor() {
        //log.info("Navigating to Age Factor from Blood Pressure page...");
        topNav.goToAgeFactor();
        //log.info("Navigation to Age Factor complete.");
    }

    public void goToTeamDetails() {
        //log.info("Navigating to Team Details from Blood Pressure page...");
        topNav.goToTeamDetails();
        //log.info("Navigation to Team Details complete.");
    }
}

