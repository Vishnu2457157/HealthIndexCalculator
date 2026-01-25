package utils;

import org.openqa.selenium.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static File targetFile(String baseName) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        File dir = new File(System.getProperty("user.dir") + File.separator + "reports" + File.separator + "screenshots");
        if (!dir.exists()) dir.mkdirs();
        return new File(dir, baseName + "_" + ts + ".png").getAbsoluteFile();
    }

    /** Take a viewport/page screenshot and save it, returning the File. */
    public static File takeScreenshotToFile(WebDriver driver, String baseName) {
        File dest = targetFile(baseName);
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(src.toPath(), dest.toPath());
            return dest;
        } catch (IOException e) {
            return new File("Screenshot save failed: " + e.getMessage());
        }
    }

    public static File takeScreenshotofCalculator(WebDriver driver, String baseName){
        WebElement calculator= driver.findElement(By.cssSelector(".calc"));
        JavascriptExecutor js= (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);",calculator);
        File src=calculator.getScreenshotAs(OutputType.FILE);
        File dest= targetFile(baseName);
        try {
            Files.copy(src.toPath(), dest.toPath());
            return dest;
        } catch (IOException e) {
            return new File("Screenshot save failed: " + e.getMessage());
        }
    }

    /** Build a safe, readable filename base from inputs (age, pulse, bp, testType). */
    public static String buildName(String age, String pulse, String bp, String testType) {
        String safe = String.format("age_%s__pulse_%s__bp_%s__type_%s",
                sanitize(age), sanitize(pulse), sanitize(bp), sanitize(testType));

        return safe.length() > 150 ? safe.substring(0, 150) : safe;
    }

    private static String sanitize(String s) {
        if (s == null) return "null";
        return s.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
