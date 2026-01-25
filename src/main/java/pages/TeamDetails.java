
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;


import java.time.Duration;

public class TeamDetails {
   // private static final Logger log = LoggerFactory.getLogger(TeamDetails.class);

    WebDriver driver;
    TopNavigation topNav;
    private final WebDriverWait wait;

    public TeamDetails(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.topNav = new TopNavigation(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
       // log.debug("TeamDetails initialized with driver {}", driver);
    }

    @FindBy(xpath="//footer/h3")
    WebElement heading;

    @FindBy(tagName = "ul")
    WebElement teamMembers;

    public boolean isHeadingPresent(){
        try {
            //log.info("Waiting for Team Details heading to be visible...");
            wait.until(ExpectedConditions.visibilityOf(heading));
            boolean present = heading.isDisplayed();
           // log.info("Team Details heading present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Team Details heading not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTeamDetailsPresent(){
        try {
            //log.info("Waiting for Team members list to be visible...");
            wait.until(ExpectedConditions.visibilityOf(teamMembers));
            boolean present = teamMembers.isDisplayed();
            //log.info("Team members list present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Team members list not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTopNavPresent() {
        boolean present = topNav.isNavBarPresent();
        //log.info("Top navigation present: {}", present);
        return present;
    }

    public void goToHome() {
       // log.info("Navigating to Home from Team Details page...");
        topNav.goToHome();
       // log.info("Navigation to Home complete.");
    }

    public void goToHeartPulse() {
       // log.info("Navigating to Heart Pulse from Team Details page...");
        topNav.goToHeartPulse();
      //  log.info("Navigation to Heart Pulse complete.");
    }

    public void goToAgeFactor() {
        //log.info("Navigating to Age Factor from Team Details page...");
        topNav.goToAgeFactor();
        //log.info("Navigation to Age Factor complete.");
    }

    public void goToBloodPressure() {
        //log.info("Navigating to Blood Pressure from Team Details page...");
        topNav.goToBloodPressure();
        //log.info("Navigation to Blood Pressure complete.");
    }
}
