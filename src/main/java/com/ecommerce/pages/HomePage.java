package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;
    
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
    public boolean isHomePageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("app_logo")));
            System.out.println("✅ Home page verified!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Home page not displayed");
            return false;
        }
    }
    
    public void goToCart() {
        System.out.println("🛒 Navigating to cart...");
        sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link")));
        driver.findElement(By.className("shopping_cart_link")).click();
        sleep(1500);
        System.out.println("✅ Cart page opened");
    }
    
    public void logout() {
        System.out.println("🚪 Logging out...");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn")));
        driver.findElement(By.id("react-burger-menu-btn")).click();
        sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
        driver.findElement(By.id("logout_sidebar_link")).click();
        System.out.println("✅ Logged out");
        sleep(1500);
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}