
package tests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TopNavigation;

import static org.testng.AssertJUnit.assertTrue;

public class UrlValidationTest extends BaseTest {


    @BeforeClass
    public void setUp() {
        log.info("Initializing page objects: TopNavigation, HomePage");
        tn=new TopNavigation(driver);
        home=new HomePage(driver);
        log.info("Setup completed for UrlValidationTest");
    }

    @Test
    public void isURLWorking() {
        log.info("=== Starting 'isURLWorking' test ===");

        boolean navBarPresent = tn.isNavBarPresent();
        boolean leftImagePresent = home.isLeftImagePresent();
        boolean calculatorPresent = home.isCalculatorPresent();

        log.info("NavBar present? " + navBarPresent);
        log.info("Left image present? " + leftImagePresent);
        log.info("Calculator present? " + calculatorPresent);


        assertTrue("Home page should be loaded", navBarPresent && leftImagePresent && calculatorPresent);

        log.info("PASS: Home page loaded successfully with all required elements");

        log.info("=== Completed 'isURLWorking' test ===");
    }

}
