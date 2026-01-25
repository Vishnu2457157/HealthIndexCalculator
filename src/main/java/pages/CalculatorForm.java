
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;


import java.time.Duration;

public class CalculatorForm {
    //private static final Logger log = LoggerFactory.getLogger(CalculatorForm.class);

    private final WebDriverWait wait;

    public CalculatorForm(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //log.debug("CalculatorForm initialized with driver {}", driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @FindBy(id = "age") private WebElement ageField;
    @FindBy(id = "pulse") private WebElement pulseField;
    @FindBy(id = "bp") private WebElement bpField;
    @FindBy(tagName = "button") private WebElement calculateBtn;
    @FindBy(xpath = "//h2[normalize-space()='Health Index Calculator']")
    private WebElement formHeading;

    public boolean isFormHeadingPresent() {
        try {
            //log.info("Waiting for form heading to be visible...");
            wait.until(ExpectedConditions.visibilityOf(formHeading));
            boolean present = formHeading.isDisplayed();
            //log.info("Form heading present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Form heading not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isAgeFieldPresent() {
        try {
            //log.info("Waiting for Age field to be visible...");
            wait.until(ExpectedConditions.visibilityOf(ageField));
            boolean present = ageField.isDisplayed();
            //log.info("Age field present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Age field not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isPulseRateFieldPresent() {
        try {
            //log.info("Waiting for Pulse field to be visible...");
            wait.until(ExpectedConditions.visibilityOf(pulseField));
            boolean present = pulseField.isDisplayed();
          //  log.info("Pulse field present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Pulse field not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isBloodPressureFieldPresent() {
        try {
           // log.info("Waiting for Blood Pressure field to be visible...");
            wait.until(ExpectedConditions.visibilityOf(bpField));
            boolean present = bpField.isDisplayed();
            //log.info("Blood Pressure field present: {}", present);
            return present;
        } catch (Exception e) {
            //log.error("Blood Pressure field not present/visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isButtonPresentAndClickable() {
        try {
            //log.info("Waiting for Calculate button to be clickable...");
            wait.until(ExpectedConditions.elementToBeClickable(calculateBtn));
            boolean state = calculateBtn.isDisplayed() && calculateBtn.isEnabled();
           // log.info("Calculate button present and clickable: {}", state);
            return state;
        } catch (Exception e) {
            //log.error("Calculate button not clickable/present: {}", e.getMessage());
            return false;
        }
    }

    public void enterAge(String age) {
       // log.info("Entering Age: {}", age);
        wait.until(ExpectedConditions.visibilityOf(ageField));
        ageField.clear();
        ageField.sendKeys(age);
        //log.debug("Age field now contains: {}", ageField.getAttribute("value"));
    }

    public void enterPulse(String pulse) {
        //log.info("Entering Pulse: {}", pulse);
        wait.until(ExpectedConditions.visibilityOf(pulseField));
        pulseField.clear();
        pulseField.sendKeys(pulse);
        //log.debug("Pulse field now contains: {}", pulseField.getAttribute("value"));
    }

    public void enterBloodPressure(String bp) {
       // log.info("Entering Blood Pressure: {}", bp);
        wait.until(ExpectedConditions.visibilityOf(bpField));
        bpField.clear();
        bpField.sendKeys(bp);
       // log.debug("BP field now contains: {}", bpField.getAttribute("value"));
    }

    public void clickCalculate() {
       // log.info("Clicking Calculate button...");
        wait.until(ExpectedConditions.elementToBeClickable(calculateBtn));
        calculateBtn.click();
       // log.info("Calculate button clicked.");
    }

    public String isAgeFieldTakingValue(String age) {
       // log.info("Verifying Age field takes value: {}", age);
        enterAge(age);
        String value = ageField.getAttribute("value");
       // log.debug("Age field value read back: {}", value);
        return value;
    }

    public String isPulseRateFieldTakingValue(String pulse) {
        //log.info("Verifying Pulse field takes value: {}", pulse);
        enterPulse(pulse);
        String value = pulseField.getAttribute("value");
       // log.debug("Pulse field value read back: {}", value);
        return value;
    }

    public String isBloodPressureFieldTakingValue(String bp) {
       // log.info("Verifying BP field takes value: {}", bp);
        enterBloodPressure(bp);
        String value = bpField.getAttribute("value");
        //log.debug("BP field value read back: {}", value);
        return value;
    }
}
