package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CartPage {
    WebDriver driver;
    WebDriverWait wait;
    
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
    public boolean isProductInCart(String productName) {
        System.out.println("🔍 Verifying product in cart: " + productName);
        sleep(1000);
        
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_name")));
        List<WebElement> items = driver.findElements(By.className("inventory_item_name"));
        
        for (WebElement item : items) {
            if (item.getText().equalsIgnoreCase(productName)) {
                System.out.println("✅ Product verified in cart: " + productName);
                return true;
            }
        }
        System.out.println("❌ Product not found in cart");
        return false;
    }
    
    public void proceedToCheckout() {
        System.out.println("💳 Proceeding to checkout...");
        sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));
        driver.findElement(By.id("checkout")).click();
        sleep(1500);
        System.out.println("✅ Checkout page opened");
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}