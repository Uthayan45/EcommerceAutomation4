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

    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";
    private static final String PRODUCT_NAME = "Sauce Labs Backpack";

    @Test
    public void testSearchAndFilter() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 3: SEARCH AND FILTER TEST");
        System.out.println("=".repeat(60));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not loaded!");

        SearchPage searchPage = new SearchPage(driver);
        searchPage.applyFilter("Price (high to low)");
        sleep(2000);

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
        loginPage.login(USERNAME, PASSWORD);

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not loaded!");

        SearchPage searchPage = new SearchPage(driver);
        Assert.assertTrue(searchPage.isProductDisplayed(PRODUCT_NAME), "Product not found!");

        searchPage.addToCart(PRODUCT_NAME);
        sleep(2000);

        homePage.goToCart();
        sleep(2000);

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isProductInCart(PRODUCT_NAME), "Product not in cart!");

        System.out.println("\nTEST PASSED: Product added to cart successfully!\n");
        sleep(2000);
    }

    @Test
    public void testCompletePurchase() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 5: COMPLETE PURCHASE FLOW");
        System.out.println("=".repeat(60));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not loaded!");

        SearchPage searchPage = new SearchPage(driver);
        Assert.assertTrue(searchPage.isProductDisplayed(PRODUCT_NAME), "Product not found!");

        searchPage.addToCart(PRODUCT_NAME);
        sleep(2000);

        homePage.goToCart();
        sleep(2000);

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isProductInCart(PRODUCT_NAME), "Product not found in cart before checkout!");

        cartPage.proceedToCheckout();
        sleep(2000);

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterCheckoutDetails("John", "Doe", "12345");
        sleep(1000);
        checkoutPage.finishCheckout();
        sleep(2000);

        Assert.assertTrue(checkoutPage.isCheckoutSuccessful(), "Checkout failed!");

        System.out.println("\nTEST PASSED: Purchase completed successfully!\n");
        sleep(2000);
    }
}