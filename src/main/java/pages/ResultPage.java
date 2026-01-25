package pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import java.time.Duration;

public class ResultPage {
    WebDriver driver;
    TopNavigation topNav;
    public ResultPage(WebDriver driver){
        this.driver=driver;
        PageFactory.initElements(driver,this);
        //log.debug("ResultPage initialized.");
        this.topNav=new TopNavigation(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    @FindBy(xpath="//div[@class='result-container']")
    WebElement resultBox;
    @FindBy(tagName = "h2")
    WebElement heading;
    @FindBy(id="healthbox")
    WebElement result;
    @FindBy(id="feedback")
    WebElement feedbackBox;
    public boolean isLogoPresent(){
        //log.info("Logo Present:{}",topNav.isLogoPresent());
        return topNav.isLogoPresent();
    }
    public boolean isLogoTextPresent(){
        //log.info("Logo Text Present:{}",topNav.isLogoTextPresent());
        return topNav.isLogoTextPresent();
    }
    public boolean isResultBoxPresent(){
       // log.info("Result Bor present:{}",resultBox.isDisplayed());
        return resultBox.isDisplayed();
    }
    public boolean isHeadingPresent() {
       // log.info("Heading present:{}",heading.isDisplayed());
        return heading.isDisplayed();
    }
    public boolean isResultDisplayed(){
       // log.info("Result Displayed:{}",result.isDisplayed());
        return result.isDisplayed();
    }
    public boolean isFeedbackBoxPresent(){
       // log.info("Feedback Box present:{}",feedbackBox.isDisplayed());
        return feedbackBox.isDisplayed();
    }
}
