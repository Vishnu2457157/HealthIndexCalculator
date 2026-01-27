
package tests;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HeartPulsePage;
import pages.TopNavigation;

public class HeartPulsePageValidationTest extends BaseTest {




    @BeforeClass
    public void setUp() {
        log.info("Initializing HeartPulsePage and TopNavigation with driver {}", driver);

        hp=new HeartPulsePage(driver);
        tn=new TopNavigation(driver);
        log.info("Navigating to 'Heart Pulse' via top navigation");
        tn.goToHeartPulse();

        log.info("Arrived at Heart Pulse page. Current URL: {}", driver.getCurrentUrl());
    }

    @Test(priority=0)
    public void isTopNavPresent() {
        log.info("Verifying top navigation presence on Heart Pulse page");
        boolean present = hp.isTopNavPresent();
        log.debug("Top nav present? {}", present);
        assert present;
        log.info("'isTopNavPresent' assertion passed");
    }

    @Test(priority = 1)
    public void isHeadingPresent() {
        log.info("Checking Heart Pulse page heading presence");
        boolean present = hp.isHeadingPresent();
        log.debug("Heading present? {}", present);
        assert present;
        log.info("'isHeadingPresent' assertion passed");
    }

    @Test(priority = 2)
    public void gotoHome() throws InterruptedException {
        log.info("Navigating to Home from Heart Pulse page");
        hp.goToHome();

        String url = driver.getCurrentUrl();
        log.info("URL after goToHome(): {}", url);

        assert url.contains("#container");
        log.info("'gotoHome' assertion passed");
    }

    @Test(priority = 3)
    public void gotoBloodPressure() {
        log.info("Navigating to Blood Pressure from Heart Pulse page");
        hp.goToBloodPressure();

        String url = driver.getCurrentUrl();
        log.info("URL after goToBloodPressure(): {}", url);

        assert url.contains("#bloodPressure");
        log.info("'gotoBloodPressure' assertion passed");
    }

    @Test(priority = 4)
    public void gotoAgeFactor() {
        log.info("Navigating to Age Factor from Heart Pulse page");
        hp.goToAgeFactor();

        String url = driver.getCurrentUrl();
        log.info("URL after goToAgeFactor(): {}", url);

        assert url.contains("#ageFactor");
        log.info("'gotoAgeFactor' assertion passed");
    }

    @Test(priority = 5)
    public void gotoTeamDetails() {
        log.info("Navigating to Team Details from Heart Pulse page");
        hp.goToTeamDetails();

        String url = driver.getCurrentUrl();
        log.info("URL after goToTeamDetails(): {}", url);

        assert url.contains("#foot");
        log.info("'gotoTeamDetails' assertion passed");
    }

}
