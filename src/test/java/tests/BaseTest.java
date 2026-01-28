package tests;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;
import pages.*;
import utils.ExcelDataManager;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    Logger log = LoggerFactory.getLogger(this.getClass());
    Properties p;
    AgeFactorPage af;
    TopNavigation tn;
    BloodPressurePage bp;
    CalculatorForm cf;
    ExcelDataManager df;
    TopNavigation tp;
    WebDriverWait wait;
    HeartPulsePage hp;
    HomePage home;
    ResultPage res;
    TeamDetails td;

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser","os"})
    public void setUp(ITestContext context,  @Optional("chrome") String browser,@Optional("windows") String os) throws IOException {
        FileReader file=new FileReader("src/test/resources/config.properties");
        p=new Properties();
        p.load(file);

        boolean headless = Boolean.parseBoolean(p.getProperty("headless", "false"));

        if(p.getProperty("execution_env").equalsIgnoreCase("remote")){
            try {
                if (browser.equalsIgnoreCase("chrome")) {
                    ChromeOptions options = new ChromeOptions();
                    if (headless) {
                        options.addArguments("--headless=new");
                        // fallback for older drivers: options.setHeadless(true);
                    }
                    if (os.equalsIgnoreCase("windows")) {
                        options.setCapability("platformName", Platform.WIN11);
                    } else if (os.equalsIgnoreCase("mac")){
                        options.setCapability("platformName", Platform.MAC);
                    } else {
                        System.out.println("No matching OS");
                        return;
                    }
                    driver = new RemoteWebDriver(new URL("http://10.232.48.253:4444/wd/hub"), options);
                } else if (browser.equalsIgnoreCase("edge")) {
                    EdgeOptions options = new EdgeOptions();
                    if (headless) {
                        options.addArguments("--headless=new");
                    }
                    if (os.equalsIgnoreCase("windows")) {
                        options.setCapability("platformName", Platform.WIN11);
                    } else if (os.equalsIgnoreCase("mac")){
                        options.setCapability("platformName", Platform.MAC);
                    } else {
                        System.out.println("No matching OS");
                        return;
                    }
                    driver = new RemoteWebDriver(new URL("http://10.232.48.253:4444/wd/hub"), options);
                } else {
                    System.out.println("Invalid browser");
                    Reporter.log("Invalid browser parameter: " + browser, true);
                    return;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create remote driver", e);
            }
        }
        else if(p.getProperty("execution_env").equalsIgnoreCase("local")){
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) {
                        edgeOptions.addArguments("--headless=new");
                    }
                    driver = new EdgeDriver(edgeOptions);
                    break;
                default:
                    System.out.println("Invalid browser");
                    Reporter.log("Invalid browser parameter: " + browser, true);
                    return;
            }
        }
        String baseUrl=p.getProperty("appURL");
        log.info("{} Browser started", browser);
        Reporter.log(browser + " browser started", true);
        driver.manage().window().maximize();
        driver.get(baseUrl);
        context.setAttribute("driver", driver);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown(ITestContext context) {
        log.info("Tearing down webdriver");
        driver.quit();
    }
}
