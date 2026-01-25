
package tests;
import org.testng.annotations.*;
import pages.BloodPressurePage;
import pages.TopNavigation;

import static org.testng.Assert.assertTrue;

public class BloodPressurePageValidationTest extends BaseTest {

    private BloodPressurePage bp;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        log.info("Initializing BloodPressurePage and TopNavigation...");

        bp = new BloodPressurePage(driver);
        TopNavigation tp = new TopNavigation(driver);

        log.info("Navigating to 'Blood Pressure' via top navigation...");
        tp.goToBloodPressure();

        // Synchronize: wait until Blood Pressure page unique element is visible (handled in POM ideally)

        log.info("Arrived at Blood Pressure page. Current URL: {}", driver.getCurrentUrl());
    }

    @Test(priority = 1)
    public void isTopNavPresent() {
        log.info("=== Starting 'isTopNavPresent' test ===");
        boolean present = bp.isTopNavPresent();
        assertTrue(present, "Nav bar should present on Blood Pressure page");
        log.info("PASS: Top navigation presence assertion passed.");
        log.info("=== Completed 'isTopNavPresent' test ===");
    }

    @Test(priority = 2, dependsOnMethods = "isTopNavPresent")
    public void isHeadingPresent() {
        log.info("=== Starting 'isHeadingPresent' test ===");
        boolean present = bp.isHeadingPresent();
        assertTrue(present, "Heading should present");
        log.info("PASS: Heading presence assertion passed.");
        log.info("=== Completed 'isHeadingPresent' test ===");
    }

    @Test(priority = 3, dependsOnMethods = "isHeadingPresent")
    public void isInfoPresent() {
        log.info("=== Starting 'isInfoPresent' test ===");
        boolean present = bp.isInfoPresent();
        assertTrue(present, "Information should present");
        log.info("PASS: Information block presence assertion passed.");
        log.info("=== Completed 'isInfoPresent' test ===");
    }

    @Test(priority = 4, dependsOnMethods = "isInfoPresent")
    public void gotoHome() {
        log.info("Navigating to Home via BloodPressurePage.goToHome()...");
        bp.goToHome();
        String url = driver.getCurrentUrl();
        log.info("URL after goToHome(): {}", url);
        assertTrue(url.contains("#container"), "Expected URL to contain '#container' for Home");
        log.info("PASS: Home navigation assertion passed.");
    }

    @Test(priority = 5, dependsOnMethods = "gotoHome")
    public void gotoHeartPulse() {
        log.info("Navigating to Heart Pulse via BloodPressurePage.goToHeartPulse()...");
        bp.goToHeartPulse();
        String url = driver.getCurrentUrl();
        log.info("URL after goToHeartPulse(): {}", url);
        assertTrue(url.contains("#pulseRate"), "Expected URL to contain '#pulseRate' for Heart Pulse");
        log.info("PASS: Heart Pulse navigation assertion passed.");
    }

    @Test(priority = 6, dependsOnMethods = "gotoHeartPulse")
    public void gotoAgeFactorPage() {
        log.info("Navigating to Age Factor via BloodPressurePage.goToAgeFactor()...");
        bp.goToAgeFactor();
        String url = driver.getCurrentUrl();
        log.info("URL after goToAgeFactor(): {}", url);
        assertTrue(url.contains("#ageFactor"), "Expected URL to contain '#ageFactor' for Age Factor");
        log.info("PASS: Age Factor navigation assertion passed.");
    }

    @Test(priority = 7, dependsOnMethods = "gotoAgeFactorPage")
    public void gotoTeamDetails() {
        log.info("Navigating to Team Details via BloodPressurePage.goToTeamDetails()...");
        bp.goToTeamDetails();
        String url = driver.getCurrentUrl();
        log.info("URL after goToTeamDetails(): {}", url);
        assertTrue(url.contains("#foot"), "Expected URL to contain '#foot' for Team Details");
        log.info("PASS: Team Details navigation assertion passed.");
    }

}
