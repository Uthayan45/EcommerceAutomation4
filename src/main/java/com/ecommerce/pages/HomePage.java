package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By appLogo = By.className("app_logo");
    private By cartLink = By.className("shopping_cart_link");
    private By menuButton = By.id("react-burger-menu-btn");
    private By logoutLink = By.id("logout_sidebar_link");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public boolean isHomePageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(appLogo));
            System.out.println("Home page verified!");
            return true;
        } catch (Exception e) {
            System.out.println("Home page not displayed");
            return false;
        }
    }

    public void goToCart() {
        System.out.println("Navigating to cart...");

        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartLink));

        try {
            cart.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cart);
        }

        wait.until(ExpectedConditions.urlContains("cart"));
        System.out.println("Cart page opened");
    }

    public void logout() {
        System.out.println("Logging out...");

        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();

        System.out.println("Logged out");
    }
}