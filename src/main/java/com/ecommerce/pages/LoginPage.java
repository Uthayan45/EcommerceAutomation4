package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
    public void login(String username, String password) {
        System.out.println("📝 Entering username...");
        sleep(1000);
        
        // Find username field and enter
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);
        System.out.println("   Username: " + username);
        sleep(500);
        
        System.out.println("📝 Entering password...");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        System.out.println("   Password: *******");
        sleep(500);
        
        System.out.println("🖱️ Clicking login button...");
        driver.findElement(By.id("login-button")).click();
        System.out.println("⏳ Waiting for login to complete...");
        sleep(3000);
    }
    
    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.urlContains("inventory"));
            return driver.getCurrentUrl().contains("inventory");
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        return driver.findElement(By.cssSelector("[data-test='error']")).getText();
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}