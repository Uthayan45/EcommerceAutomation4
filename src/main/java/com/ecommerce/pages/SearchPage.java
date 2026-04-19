package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By sortDropdown = By.className("product_sort_container");
    private By inventoryItems = By.className("inventory_item");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Timeout increased to 30s
    }

    public void applyFilter(String filterValue) {
        System.out.println("Applying filter: " + filterValue);
        WebElement dropdownElement = wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByVisibleText(filterValue);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
        System.out.println("Filter applied: " + filterValue);
    }

    public void addToCart(String productName) {
        System.out.println("Attempting to add: " + productName);

        // Jenkins stabilization wait
        try { Thread.sleep(2000); } catch (Exception e) {}

        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));

        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText().trim();

            if (name.equalsIgnoreCase(productName)) {
                WebElement actionButton = item.findElement(By.tagName("button"));

                // Safety: Check if already added
                if (actionButton.getText().toLowerCase().contains("remove")) {
                    System.out.println(productName + " already in cart.");
                    return;
                }

                // Force Scroll
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", actionButton);
                try { Thread.sleep(1000); } catch (Exception e) {}

                // THE FIX: Direct JS Click with built-in retry
                for (int i = 0; i < 3; i++) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionButton);
                    System.out.println("Click Attempt " + (i + 1) + " for: " + productName);

                    try {
                        // Wait up to 5 seconds for the text to change per attempt
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> {
                            return actionButton.getText().toLowerCase().contains("remove");
                        });
                        System.out.println("Successfully added: " + productName);
                        return; // Exit if success
                    } catch (Exception e) {
                        System.out.println("Waiting for button state change...");
                    }
                }

                throw new RuntimeException("CRITICAL FAILURE: Button did not toggle to 'Remove' for " + productName);
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    public boolean isProductDisplayed(String productName) {
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_name")));
        for (WebElement product : products) {
            if (product.getText().trim().equalsIgnoreCase(productName)) return true;
        }
        return false;
    }

    public int getProductCount() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems)).size();
    }
}