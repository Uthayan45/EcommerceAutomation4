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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void applyFilter(String filterValue) {
        System.out.println("Applying filter: " + filterValue);
        WebElement dropdownElement = wait.until(
                ExpectedConditions.elementToBeClickable(sortDropdown));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByVisibleText(filterValue);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
        System.out.println("Filter applied: " + filterValue);
    }

    public void addToCart(String productName) {
        System.out.println("Adding product to cart: " + productName);

        // Wait until products are visible
        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));

        boolean productFound = false;

        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText().trim();

            if (name.equalsIgnoreCase(productName)) {
                productFound = true;
                WebElement addToCartButton = item.findElement(By.tagName("button"));

                // Scroll element to middle of the screen to avoid Jenkins overlay issues
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block:'center'});", addToCartButton);

                // Adding a small sync sleep for Jenkins environment stability
                try { Thread.sleep(500); } catch (InterruptedException e) {}

                // Try regular click, if fails use Javascript Click
                try {
                    addToCartButton.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);
                }

                // FIXED WAIT: Custom lambda to check if button text changed to 'Remove'
                // This is more reliable than standard ExpectedConditions in Jenkins
                wait.until(d -> {
                    String btnText = addToCartButton.getText().toLowerCase();
                    return btnText.contains("remove");
                });

                System.out.println("Added to cart: " + productName);
                return;
            }
        }

        if (!productFound) {
            throw new RuntimeException("Product not found to add: " + productName);
        }
    }

    public boolean isProductDisplayed(String productName) {
        List<WebElement> products = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.className("inventory_item_name")));

        for (WebElement product : products) {
            if (product.getText().trim().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    public int getProductCount() {
        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
        return items.size();
    }
}