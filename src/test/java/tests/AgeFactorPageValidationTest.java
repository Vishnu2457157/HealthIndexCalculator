
package tests;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.AgeFactorPage;
import pages.TopNavigation;

import static org.testng.Assert.assertTrue;

public class AgeFactorPageValidationTest extends BaseTest {
    AgeFactorPage af;
    TopNavigation tn;

    @BeforeClass
    public void setup() {
        log.info("Initializing AgeFactorPage and TopNavigation for driver: {}", driver);

        this.af = new AgeFactorPage(driver);
        this.tn = new TopNavigation(driver);

        log.info("Navigating to 'Age Factor' via top navigation");
        tn.goToAgeFactor();

        log.info("Arrived at Age factor page");
    }

    @Test(priority = 1)
    public void isTopNavPresent() {
        log.info("Verifying top navigation presence on Age Factor page");
        Reporter.log("Checking top navigation presence on Age Factor page...", true);
        boolean present = af.isTopNavPresent();
        log.debug("Top nav present? {}", present);
        Reporter.log("Top nav present? " + present, true);
        assertTrue(present, "Nav bar should present");
        log.info("PASS: Top navigation presence assertion passed");
        Reporter.log("PASS: Top navigation presence assertion passed", true);
    }
    @Test(priority = 2)
    public void isHeadingPresent() {
        log.info("Checking Age Factor page heading presence (and style if applicable)");
        Reporter.log("Checking heading presence on Age Factor page...", true);
        boolean present = af.isHeadingPresent();
        log.debug("Heading present? {}", present);
        Reporter.log("Heading present? " + present, true);
        assertTrue(present, "Heading should present");
        log.info("PASS: Heading presence assertion passed");
        Reporter.log("PASS: Heading presence assertion passed", true);
    }
    @Test(priority = 3)
    public void isInfoPresent() {
        log.info("Validating informational block (white text per spec) on Age Factor page");
        Reporter.log("Validating informational block presence...", true);
        boolean present = af.isInfoPresent();
        log.debug("Info block present? {}", present);
        Reporter.log("Info block present? " + present, true);
        assertTrue(present, "Information should present");
        log.info("PASS: Info block presence assertion passed");
        Reporter.log("PASS: Info block presence assertion passed", true);
    }

    @Test(priority = 4)
    public void gotoHome() {
        log.info("Navigating to Home via AgeFactorPage.goToHome()");
        Reporter.log("Clicking Home...", true);
        af.goToHome();
        String url = driver.getCurrentUrl();
        log.debug("URL after goToHome(): {}", url);
        Reporter.log("URL after goToHome(): " + url, true);
        assertTrue(url.contains("#container"), "Expected URL to contain '#container' for Home");
        log.info("PASS: Home navigation assertion passed");
        Reporter.log("PASS: Home navigation assertion passed", true);
    }

    @Test(priority = 5)
    public void gotoHeartPulse() {
        log.info("Navigating to Heart Pulse via AgeFactorPage.goToHeartPulse()");
        Reporter.log("Clicking Heart Pulse...", true);
        af.goToHeartPulse();
        String url = driver.getCurrentUrl();
        log.debug("URL after goToHeartPulse(): {}", url);
        Reporter.log("URL after goToHeartPulse(): " + url, true);
        assertTrue(url.contains("#pulseRate"), "Expected URL to contain '#pulseRate' for Heart Pulse");
        log.info("PASS: Heart Pulse navigation assertion passed");
        Reporter.log("PASS: Heart Pulse navigation assertion passed", true);
    }

    @Test(priority = 6)
    public void gotoBloodPressure() {
        log.info("Navigating to Blood Pressure via AgeFactorPage.goToBloodPressure()");
        Reporter.log("Clicking Blood Pressure...", true);
        af.goToBloodPressure();
        String url = driver.getCurrentUrl();
        log.debug("URL after goToBloodPressure(): {}", url);
        Reporter.log("URL after goToBloodPressure(): " + url, true);
        assertTrue(url.contains("#bloodPressure"), "Expected URL to contain '#bloodPressure' for Blood Pressure");
        log.info("PASS: Blood Pressure navigation assertion passed");
        Reporter.log("PASS: Blood Pressure navigation assertion passed", true);
    }

    @Test(priority = 7)
    public void gotoTeamDetails() {
        log.info("Navigating to Team Details via AgeFactorPage.goToTeamDetails()");
        Reporter.log("Clicking Team Details...", true);
        af.goToTeamDetails();
        String url = driver.getCurrentUrl();
        log.debug("URL after goToTeamDetails(): {}", url);
        Reporter.log("URL after goToTeamDetails(): " + url, true);
        assertTrue(url.contains("#foot"), "Expected URL to contain '#foot' for Team Details");
        log.info("PASS: Team Details navigation assertion passed");
        Reporter.log("PASS: Team Details navigation assertion passed", true);
    }

}

