package com.ecommerce.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) {
            System.out.println("⚠️ Cannot capture screenshot - Driver is null");
            return null;
        }
        
        try {
            String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String destination = System.getProperty("user.dir") + "/screenshots/" + screenshotName + "_" + dateName + ".png";
            
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(System.getProperty("user.dir") + "/screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
                System.out.println("📁 Created screenshots directory");
            }
            
            FileUtils.copyFile(source, new File(destination));
            System.out.println("📸 Screenshot saved: " + destination);
            return destination;
        } catch (IOException e) {
            System.out.println("❌ Failed to save screenshot: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("❌ Error capturing screenshot: " + e.getMessage());
            return null;
        }
    }
}