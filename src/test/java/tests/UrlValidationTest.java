
package tests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TopNavigation;

import static org.testng.AssertJUnit.assertTrue;

public class UrlValidationTest extends BaseTest {
    TopNavigation tp;
    HomePage hp;

    @BeforeClass
    public void setUp() {
        log.info("Initializing page objects: TopNavigation, HomePage");

        tp = new TopNavigation(driver);
        hp = new HomePage(driver);

        log.info("Setup completed for UrlValidationTest");
    }

    @Test
    public void isURLWorking() {
        log.info("=== Starting 'isURLWorking' test ===");

        boolean navBarPresent = tp.isNavBarPresent();
        boolean leftImagePresent = hp.isLeftImagePresent();
        boolean calculatorPresent = hp.isCalculatorPresent();

        log.info("NavBar present? " + navBarPresent);
        log.info("Left image present? " + leftImagePresent);
        log.info("Calculator present? " + calculatorPresent);


        assertTrue("Home page should be loaded", navBarPresent && leftImagePresent && calculatorPresent);

        log.info("PASS: Home page loaded successfully with all required elements");

        log.info("=== Completed 'isURLWorking' test ===");
    }

}
