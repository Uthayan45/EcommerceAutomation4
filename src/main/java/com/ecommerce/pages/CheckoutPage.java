package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {
    WebDriver driver;
    WebDriverWait wait;
    
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
    public void enterCheckoutDetails(String firstName, String lastName, String postalCode) {
        System.out.println("📝 Entering checkout details...");
        sleep(1000);
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        driver.findElement(By.id("first-name")).sendKeys(firstName);
        System.out.println("   First Name: " + firstName);
        sleep(500);
        
        driver.findElement(By.id("last-name")).sendKeys(lastName);
        System.out.println("   Last Name: " + lastName);
        sleep(500);
        
        driver.findElement(By.id("postal-code")).sendKeys(postalCode);
        System.out.println("   Postal Code: " + postalCode);
        sleep(1000);
        
        driver.findElement(By.id("continue")).click();
        System.out.println("✅ Checkout details submitted");
        sleep(2000);
    }
    
    public void finishCheckout() {
        System.out.println("💳 Finishing checkout...");
        sleep(1000);
        
        // Wait for finish button and click
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish")));
        driver.findElement(By.id("finish")).click();
        System.out.println("✅ Checkout finished");
        sleep(3000);
    }
    
    public boolean isCheckoutSuccessful() {
        try {
            System.out.println("🔍 Looking for success message...");
            
            // Wait for success message with multiple possible locators
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'THANK YOU')]"))
            ));
            
            // Try different locators to find success message
            String successMessage = "";
            try {
                successMessage = driver.findElement(By.className("complete-header")).getText();
            } catch (Exception e) {
                try {
                    successMessage = driver.findElement(By.cssSelector(".complete-header")).getText();
                } catch (Exception e2) {
                    successMessage = driver.findElement(By.xpath("//h2[contains(text(),'THANK YOU')]")).getText();
                }
            }
            
            System.out.println("📧 Success message found: " + successMessage);
            
            // Check if message contains "THANK YOU" (case insensitive)
            if (successMessage.toUpperCase().contains("THANK YOU")) {
                System.out.println("✅ Checkout verification successful!");
                return true;
            } else {
                System.out.println("❌ Success message does not contain 'THANK YOU'");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("❌ Failed to find success message: " + e.getMessage());
            
            // Check URL as fallback
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 Current URL: " + currentUrl);
            
            if (currentUrl.contains("checkout-complete")) {
                System.out.println("✅ URL indicates checkout complete!");
                return true;
            }
            
            return false;
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