package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CartPage;
import com.ecommerce.pages.CheckoutPage;
import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductFlowTest extends BaseTest {

    @Test
    public void testSearchAndFilter() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 3: SEARCH AND FILTER TEST");
        System.out.println("=".repeat(60));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not loaded!");

        SearchPage searchPage = new SearchPage(driver);
        searchPage.applyFilter("Price (high to low)");

        int productCount = searchPage.getProductCount();
        Assert.assertTrue(productCount > 0, "No products found!");

        System.out.println("\nTEST PASSED: Found " + productCount + " products!\n");
        sleep(2000);
    }

    @Test
    public void testAddToCart() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4: ADD TO CART TEST");
        System.out.println("=".repeat(60));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not loaded!");

        SearchPage searchPage = new SearchPage(driver);
        String productName = "Sauce Labs Backpack";

        Assert.assertTrue(searchPage.isProductDisplayed(productName), "Product not found!");
        searchPage.addToCart(productName);

        homePage.goToCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isProductInCart(productName), "Product not in cart!");

        System.out.println("\nTEST PASSED: Product added to cart successfully!\n");
        sleep(2000);
    }

    @Test
    public void testCompletePurchase() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 5: COMPLETE PURCHASE FLOW");
        System.out.println("=".repeat(60));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not loaded!");

        SearchPage searchPage = new SearchPage(driver);
        String productName = "Sauce Labs Backpack";

        Assert.assertTrue(searchPage.isProductDisplayed(productName), "Product not found!");
        searchPage.addToCart(productName);

        homePage.goToCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isProductInCart(productName), "Product not found in cart before checkout!");
        cartPage.proceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterCheckoutDetails("John", "Doe", "12345");
        checkoutPage.finishCheckout();

        Assert.assertTrue(checkoutPage.isCheckoutSuccessful(), "Checkout failed!");

        System.out.println("\nTEST PASSED: Purchase completed successfully!\n");
        sleep(2000);
    }
}