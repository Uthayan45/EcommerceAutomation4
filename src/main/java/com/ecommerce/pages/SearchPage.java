package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class SearchPage {
    WebDriver driver;
    WebDriverWait wait;
    
    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
    public void applyFilter(String filterValue) {
        System.out.println("🔍 Applying filter: " + filterValue);
        sleep(1500);
        
        wait.until(ExpectedConditions.elementToBeClickable(By.className("product_sort_container")));
        Select dropdown = new Select(driver.findElement(By.className("product_sort_container")));
        dropdown.selectByVisibleText(filterValue);
        
        System.out.println("⏳ Waiting for filter to apply...");
        sleep(2500);
        System.out.println("✅ Filter applied: " + filterValue);
    }
    
    public void addToCart(String productName) {
        System.out.println("🛒 Adding product to cart: " + productName);
        sleep(1000);
        
        List<WebElement> products = driver.findElements(By.className("inventory_item_name"));
        List<WebElement> buttons = driver.findElements(By.cssSelector(".btn_inventory"));
        
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getText().equalsIgnoreCase(productName)) {
                sleep(1000);
                buttons.get(i).click();
                System.out.println("✅ Added to cart: " + productName);
                break;
            }
        }
        sleep(1500);
    }
    
    public boolean isProductDisplayed(String productName) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_name")));
        List<WebElement> products = driver.findElements(By.className("inventory_item_name"));
        
        for (WebElement product : products) {
            if (product.getText().equalsIgnoreCase(productName)) {
                System.out.println("✅ Product found: " + productName);
                return true;
            }
        }
        System.out.println("❌ Product not found: " + productName);
        return false;
    }
    
    public int getProductCount() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item")));
        int count = driver.findElements(By.className("inventory_item")).size();
        System.out.println("📊 Total products found: " + count);
        return count;
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}