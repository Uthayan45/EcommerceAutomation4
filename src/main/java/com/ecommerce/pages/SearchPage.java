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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
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

        // Small wait for page stabilization
        sleep(1000);

        // Find all product items
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));

        boolean productFound = false;

        for (int i = 0; i < items.size(); i++) {
            // Re-find items each iteration to avoid stale elements
            List<WebElement> freshItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
            WebElement item = freshItems.get(i);

            String name = item.findElement(By.className("inventory_item_name")).getText().trim();

            if (name.equalsIgnoreCase(productName)) {
                productFound = true;

                // Re-find the button fresh each time
                WebElement actionButton = item.findElement(By.tagName("button"));

                // Check if already in cart
                if (actionButton.getText().toLowerCase().contains("remove")) {
                    System.out.println(productName + " already in cart.");
                    return;
                }

                // Scroll to element
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", actionButton);
                sleep(500);

                // Click using JavaScript with retry mechanism
                boolean added = false;
                for (int attempt = 1; attempt <= 3; attempt++) {
                    try {
                        System.out.println("Click attempt " + attempt + " for: " + productName);

                        // Re-find the button fresh before each click attempt
                        WebElement freshButton = driver.findElement(By.xpath("//div[contains(@class,'inventory_item')]//div[contains(text(),'" + productName + "')]/following::button[1]"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", freshButton);

                        // Wait for button text to change to "Remove"
                        sleep(1500);

                        // Verify success by checking if button now says "Remove"
                        WebElement verifyButton = driver.findElement(By.xpath("//div[contains(@class,'inventory_item')]//div[contains(text(),'" + productName + "')]/following::button[1]"));
                        if (verifyButton.getText().toLowerCase().contains("remove")) {
                            System.out.println("Successfully added: " + productName);
                            added = true;
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Attempt " + attempt + " failed, retrying...");
                        sleep(500);
                    }
                }

                if (added) {
                    return;
                } else {
                    throw new RuntimeException("Failed to add product to cart: " + productName);
                }
            }
        }

        if (!productFound) {
            throw new RuntimeException("Product not found: " + productName);
        }
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

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}