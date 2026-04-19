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

        // Jenkins slow-va irukkum pothu items load aaga wait pannanum
        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));

        boolean productFound = false;

        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText().trim();

            if (name.equalsIgnoreCase(productName)) {
                productFound = true;
                WebElement actionButton = item.findElement(By.tagName("button"));

                // 1. Safety Check: Already cart-la iruntha thirumba click panna koodathu (Toggle prevention)
                String currentText = actionButton.getText().toLowerCase();
                if (currentText.contains("remove")) {
                    System.out.println("Product '" + productName + "' is already in cart. Skipping click.");
                    return;
                }

                // 2. Element-ai screen-ku naduvula kondu varuvom (Overlap-ai thavirkka)
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", actionButton);

                // Oru chinna break - element settle aaga
                try { Thread.sleep(1000); } catch (InterruptedException e) {}

                // 3. JavaScript Click: Jenkins environment-la ithu thaan romba stable
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionButton);
                System.out.println("JS Click performed for: " + productName);

                // 4. Retry Mechanism: Button text maarura varai wait pannuvom
                boolean isUpdated = false;
                for (int retry = 0; retry < 5; retry++) {
                    try {
                        String btnText = actionButton.getText().toLowerCase();
                        if (btnText.contains("remove")) {
                            isUpdated = true;
                            break;
                        }
                        // Text maaralana, thirumba oru JS click
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionButton);
                    } catch (Exception e) {
                        // Element state change aana error varalaam, so catch and retry
                    }
                    try { Thread.sleep(2000); } catch (InterruptedException e) {}
                }

                if (isUpdated) {
                    System.out.println("Added to cart successfully: " + productName);
                    return;
                } else {
                    throw new RuntimeException("FAILED: Button did not change to 'Remove' for " + productName);
                }
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
                System.out.println("Product found on page: " + productName);
                return true;
            }
        }
        return false;
    }

    public int getProductCount() {
        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
        int count = items.size();
        System.out.println("Total products visible: " + count);
        return count;
    }
}