package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import java.time.Duration;

public class TopNavigation {
    WebDriver driver;
    //Logger log= LoggerFactory.getLogger(TopNavigation.class);
    public TopNavigation(WebDriver driver){
        this.driver=driver;
        PageFactory.initElements(driver,this);
       // log.debug("TopNavigation initialized");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    @FindBy(css="div.logo img") WebElement logo;
    @FindBy(css="div.logo a") WebElement logoText;
    @FindBy(linkText = "Home") WebElement homeField;
    @FindBy(linkText = "Heart Pulse") WebElement heartPulseField;
    @FindBy(linkText = "Blood Pressure") WebElement bloodPressureField;
    @FindBy(linkText = "Age Factor") WebElement ageFactorField;
    @FindBy(linkText = "Team Details") WebElement teamDetailsField;
    public boolean isLogoPresent(){
        boolean logoPresent= logo.isDisplayed();
        //log.info("Logo present:{}",logoPresent);
        return logoPresent;
    }
    public boolean isLogoTextPresent(){
        boolean logoTextPresent=logoText.isDisplayed();
       // log.info("Logo Text present:{}",logoTextPresent);
        return logoTextPresent;
    }
    public boolean isHomeFiledDisplayed(){
        boolean homeFieldPresent=homeField.isDisplayed();
       // log.info("Home field present:{}",homeFieldPresent);
        return homeFieldPresent ;
    }
    public boolean isHeartPulseFieldPresent(){
        boolean heartPulseFieldPresent=heartPulseField.isDisplayed();
        //log.info("HeartPulse field present:{}",heartPulseFieldPresent);
        return heartPulseFieldPresent;
    }
    public boolean isBloodPressureFieldPresent(){
        boolean bloodPressureFieldPresent=bloodPressureField.isDisplayed();
        //log.info("BloodPressure field present:{}",bloodPressureFieldPresent);
        return bloodPressureFieldPresent;
    }
    public boolean isAgeFactorFieldPresent(){
       // log.info("AgeFactor field present:{}",ageFactorField.isDisplayed());
        return ageFactorField.isDisplayed();
    }
    public boolean isTeamDetailsFieldPresent(){
        //log.info("TeamDetails field present:{}",teamDetailsField.isDisplayed());
        return teamDetailsField.isDisplayed();
    }
    public boolean isNavBarPresent(){
        boolean navBarPresent=isLogoPresent() &&
                isLogoTextPresent() &&
                isHomeFiledDisplayed() &&
                isHeartPulseFieldPresent() &&
                isBloodPressureFieldPresent() &&
                isAgeFactorFieldPresent() &&
                isTeamDetailsFieldPresent();
       // log.info("TopNavigation bar present:{}",navBarPresent);
        return navBarPresent;
    }
    public void clickLogoText() {
        logoText.click();
       // log.info("Navigated to Home Page through LogoText");
    }
    public void goToHome(){
        homeField.click();
        //log.info("Navigated to Home page ");
    }
    public void goToHeartPulse() {
        heartPulseField.click();
       // log.info("Navigated to HeartPulse page");
    }
    public void goToBloodPressure() {
        bloodPressureField.click();
        //log.info("Navigated to BloodPressure page");
    }
    public void goToAgeFactor() {
        ageFactorField.click();
       // log.info("Navigated to AgeFactor page");
    }
    public void goToTeamDetails() {
        teamDetailsField.click();
        //log.info("Navigated to TeamDetails page");
    }
}


