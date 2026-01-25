
package tests;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TopNavigation;

import static org.testng.AssertJUnit.assertTrue;

public class HomePageValidationTest extends BaseTest {
    HomePage hp;
    TopNavigation tp;

    @BeforeClass
    public void setUp() {
        log.info("Initializing HomePage and TopNavigation with driver");

        hp = new HomePage(driver);
        tp = new TopNavigation(driver);

        log.info("Navigating to Home page via TopNavigation.goToHome()");
        tp.goToHome();
        log.info("Arrived at Home page. Current URL: {}", driver.getCurrentUrl());
    }

    @Test(priority = 1)
    public void isTopNavPresent() {
        log.info("=== Starting 'isTopNavPresent' test ===");
        boolean present = hp.isTopNavPresent();
        log.debug("Top nav present? {}", present);
        assertTrue(present);
        log.info("PASS: Top navigation is present");
        log.info("=== Completed 'isTopNavPresent' test ===");
    }

    @Test(priority = 2)
    public void isLeftImagePresent() {
        log.info("=== Starting 'isLeftImagePresent' test ===");
        boolean present = hp.isLeftImagePresent();
        log.debug("Left image present? {}", present);
        assertTrue(present);
        log.info("PASS: Left image is present");
        log.info("=== Completed 'isLeftImagePresent' test ===");
    }

    @Test(priority = 3)
    public void isCalculatorPresent() {
        log.info("=== Starting 'isCalculatorPresent' test ===");
        boolean present = hp.isCalculatorPresent();
        log.debug("Calculator present? {}", present);
        assertTrue(present);
        log.info("PASS: Calculator is present");
        log.info("=== Completed 'isCalculatorPresent' test ===");
    }

    @Test(priority = 4)
    public void gotoHeartPulse() {
        log.info("Navigating to HeartPulse from Home page");
        hp.goToHeartPulse();
        String url = driver.getCurrentUrl();
        log.info("URL after goToHeartPulse(): {}", url);
        assert url.contains("#pulseRate");
        log.info("PASS: 'gotoHeartPulse' assertion passed");
    }

    @Test(priority = 5)
    public void gotoBloodPressure() {
        log.info("Navigating to Blood Pressure from Home page");
        hp.goToBloodPressure();
        String url = driver.getCurrentUrl();
        log.info("URL after goToBloodPressure(): {}", url);
        assert url.contains("#bloodPressure");
        log.info("PASS: 'gotoBloodPressure' assertion passed");
    }

    @Test(priority = 6)
    public void gotoAgeFactor() {
        log.info("Navigating to Age Factor from Home page");
        hp.goToAgeFactor();
        String url = driver.getCurrentUrl();
        log.info("URL after goToAgeFactor(): {}", url);
        assert url.contains("#ageFactor");
        log.info("PASS: 'gotoAgeFactor' assertion passed");
    }

    @Test(priority = 7)
    public void gotoTeamDetails() {
        log.info("Navigating to Team Details from Home page");
        hp.goToTeamDetails();
        String url = driver.getCurrentUrl();
        log.info("URL after goToTeamDetails(): {}", url);
        assert url.contains("#foot");
        log.info("PASS: 'gotoTeamDetails' assertion passed");
    }
}
