
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;


import java.time.Duration;

public class HeartPulsePage {
    //private static final Logger log = LoggerFactory.getLogger(HeartPulsePage.class);

    private final TopNavigation topNav;
    private final WebDriverWait wait;

    public HeartPulsePage(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.topNav = new TopNavigation(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        //log.debug("HeartPulsePage initialized with driver {}", driver);
    }

    @FindBy(xpath = "//div[@class='heading']/h2[contains(text(),'heart rate')]")
    private WebElement heading;

    @FindBy(xpath = "//div[@class='agepara']/h4")
    private WebElement subHeading;

    @FindBy(xpath = "//div[@class='agepara' and  contains(normalize-space(),'A healthy heart ')]")
    private WebElement info;

    @FindBy(xpath = "//div[@class='agepara']/img")
    private WebElement table;

    public boolean isHeadingPresent(){
        try {
            //log.info("Waiting for Heart Pulse heading to be visible...");
            wait.until(ExpectedConditions.visibilityOf(heading));
            boolean present = heading.isDisplayed();
           // log.info("Heading present: {}", present);
            return present;
        } catch (Exception e) {
           // log.error("Heading not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isSubHeadingPresent(){
        try {
           // log.info("Waiting for Heart Pulse sub-heading to be visible...");
            wait.until(ExpectedConditions.visibilityOf(subHeading));
            boolean present = subHeading.isDisplayed();
           // log.info("Sub-heading present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Sub-heading not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isInfoPresent(){
        try {
            //log.info("Waiting for informational block to be visible...");
            wait.until(ExpectedConditions.visibilityOf(info));
            boolean present = info.isDisplayed();
            //log.info("Informational block present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Informational block not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTablePresent(){
        try {
            //log.info("Waiting for heart pulse image/table to be visible...");
            wait.until(ExpectedConditions.visibilityOf(table));
            boolean present = table.isDisplayed();
            //log.info("Image/Table present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Image/Table not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTopNavPresent() {
        boolean present = topNav.isNavBarPresent();
        //log.info("Top navigation present: {}", present);
        return present;
    }

    public void goToHome() {
        //log.info("Navigating to Home from Heart Pulse page...");
        topNav.goToHome();
        //log.info("Navigation to Home complete.");
    }

    public void goToBloodPressure() {
        //log.info("Navigating to Blood Pressure from Heart Pulse page...");
        topNav.goToBloodPressure();
        //log.info("Navigation to Blood Pressure complete.");
    }

    public void goToAgeFactor() {
       // log.info("Navigating to Age Factor from Heart Pulse page...");
        topNav.goToAgeFactor();
       // log.info("Navigation to Age Factor complete.");
    }

    public void goToTeamDetails() {
        //log.info("Navigating to Team Details from Heart Pulse page...");
        topNav.goToTeamDetails();
        //log.info("Navigation to Team Details complete.");
    }
}

