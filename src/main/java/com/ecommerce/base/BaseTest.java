package com.ecommerce.base;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;


public class BaseTest {
    public WebDriver driver;
    
    @BeforeMethod
    public void setup() {
        System.out.println("\n🚀 Starting Chrome browser...");
        sleep(2000);
        
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        System.out.println("📍 Navigating to SauceDemo...");
        driver.get("https://www.saucedemo.com/");
        sleep(3000);
        System.out.println("✅ Page loaded!\n");
    }
    
    @AfterMethod
    public void tearDown() {
        System.out.println("\n⏳ Closing browser...");
        sleep(2000);
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Browser closed\n");
        }
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}