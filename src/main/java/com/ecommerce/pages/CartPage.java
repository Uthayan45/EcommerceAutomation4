package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By cartPageTitle = By.className("title");
    private By cartItems = By.className("cart_item");
    private By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public boolean isProductInCart(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageTitle));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItems));

        List<WebElement> items = driver.findElements(cartItems);

        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText().trim();
            if (name.equalsIgnoreCase(productName)) {
                System.out.println("Product found in cart: " + productName);
                return true;
            }
        }

        System.out.println("Product not found in cart: " + productName);
        return false;
    }

    public void proceedToCheckout() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageTitle));

        WebElement checkout = wait.until(
                ExpectedConditions.elementToBeClickable(checkoutButton));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", checkout);

        try {
            checkout.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkout);
        }
    }
}