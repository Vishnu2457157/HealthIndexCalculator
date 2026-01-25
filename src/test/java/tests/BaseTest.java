
package tests;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    Logger log = LoggerFactory.getLogger(this.getClass());
    Properties p;
    @BeforeClass(alwaysRun = true)
    @Parameters({"browser","os"})
    public void setUp(ITestContext context,  @Optional("chrome") String browser,@Optional("windows") String os) throws IOException {
        FileReader file=new FileReader("src/test/resources/config.properties");
        p=new Properties();
        p.load(file);
        if(p.getProperty("execution_env").equalsIgnoreCase("remote")){
            DesiredCapabilities cap=new DesiredCapabilities();
            if(os.equalsIgnoreCase("windows")){
                cap.setPlatform(Platform.WIN11);
            }
            else if(os.equalsIgnoreCase("mac")){
                cap.setPlatform(Platform.MAC);
            }
            else{
                System.out.println("No matching OS");
                return;
            }
            switch (browser.toLowerCase()) {
                case "chrome":
                    cap.setBrowserName("chrome");
                    break;
                case "edge":
                    cap.setBrowserName("MicrosoftEdge");
                    break;
                default:
                    System.out.println("Invalid browser");
                    Reporter.log("Invalid browser parameter: " + browser, true);
                    return;
            }
            driver=new RemoteWebDriver(new URL("http://10.232.48.253:4444/wd/hub"),cap);
        }
        else if(p.getProperty("execution_env").equalsIgnoreCase("local")){
            switch (browser.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
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
