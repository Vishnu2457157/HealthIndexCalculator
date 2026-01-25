
package tests;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.TeamDetails;
import pages.TopNavigation;

import static org.testng.Assert.assertTrue;

public class TeamDetailsPageValidationTest extends BaseTest {
    TeamDetails td;
    TopNavigation tn;

    @BeforeClass
    public void setup() {
        log.info("Initializing page objects: TeamDetails, TopNavigation");

        this.td = new TeamDetails(driver);
        this.tn = new TopNavigation(driver);

        log.info("Navigating to Team Details page via TopNavigation");
        tn.goToTeamDetails();

        log.info("Setup completed for TeamDetailsPageValidationTest");
    }

    @Test(priority = 1)
    public void isTopNavPresent() {
        log.info("=== Starting 'isTopNavPresent' test ===");

        boolean present = td.isTopNavPresent();
        log.info("Top navigation present? " + present);

        assertTrue(present, "Nav bar must be present");

        log.info("PASS: Top navigation is present");
        log.info("=== Completed 'isTopNavPresent' test ===");
    }

    @Test(priority = 2)
    public void isHeadingPresent() {
        log.info("=== Starting 'isHeadingPresent' test ===");

        boolean present = td.isHeadingPresent();
        log.info("Heading present? " + present);

        assertTrue(present, "Heading must be present");

        log.info("PASS: Heading is present");
        log.info("=== Completed 'isHeadingPresent' test ===");
    }

    @Test(priority = 3)
    public void isTeamDetailsPresent() {
        log.info("=== Starting 'isTeamDetailsPresent' test ===");

        boolean present = td.isTeamDetailsPresent();
        log.info("Team details present? " + present);

        assertTrue(present, "Team details should be displayed");

        log.info("PASS: Team details are displayed");
        log.info("=== Completed 'isTeamDetailsPresent' test ===");
    }

    @Test(priority = 4)
    public void gotoHome() {
        log.info("=== Starting 'gotoHome' test ===");

        td.goToHome();
        String url = driver.getCurrentUrl();

        log.info("Current URL after gotoHome(): " + url);

        assert url.contains("#container");

        log.info("PASS: 'gotoHome' assertion passed (URL contains '#container')");
        log.info("=== Completed 'gotoHome' test ===");
    }

    @Test(priority = 5)
    public void gotoBloodPressure() {
        log.info("=== Starting 'gotoBloodPressure' test ===");

        td.goToBloodPressure();
        String url = driver.getCurrentUrl();

        log.info("Current URL after gotoBloodPressure(): " + url);

        assert url.contains("#bloodPressure");

        log.info("PASS: 'gotoBloodPressure' assertion passed (URL contains '#bloodPressure')");
        log.info("=== Completed 'gotoBloodPressure' test ===");
    }

    @Test(priority = 6)
    public void gotoHeartPulse() {
        log.info("=== Starting 'gotoHeartPulse' test ===");

        td.goToHeartPulse();
        String url = driver.getCurrentUrl();

        log.info("Current URL after gotoHeartPulse(): " + url);

        assert url.contains("#pulseRate");

        log.info("PASS: 'gotoHeartPulse' assertion passed (URL contains '#pulseRate')");
        log.info("=== Completed 'gotoHeartPulse' test ===");
    }

    @Test(priority = 7)
    public void gotoAgeFactor() {
        log.info("=== Starting 'gotoAgeFactor' test ===");

        td.goToAgeFactor();
        String url = driver.getCurrentUrl();

        log.info("Current URL after gotoAgeFactor(): " + url);

        assert url.contains("#ageFactor");

        log.info("PASS: 'gotoAgeFactor' assertion passed (URL contains '#ageFactor')");
        log.info("=== Completed 'gotoAgeFactor' test ===");
    }

}
