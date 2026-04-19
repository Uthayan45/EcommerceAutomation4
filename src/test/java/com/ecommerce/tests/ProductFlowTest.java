package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductFlowTest extends BaseTest {
    
    @Test
    public void testSearchAndFilter() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 TEST 3: SEARCH AND FILTER TEST");
        System.out.println("=".repeat(60));
        
        // Login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");
        
        // Verify home page
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "❌ Home page not loaded!");
        
        // Apply filter
        SearchPage searchPage = new SearchPage(driver);
        searchPage.applyFilter("Price (high to low)");
        
        // Verify products
        int productCount = searchPage.getProductCount();
        Assert.assertTrue(productCount > 0, "❌ No products found!");
        
        System.out.println("\n✅✅✅ TEST PASSED: Found " + productCount + " products! ✅✅✅\n");
        sleep(2000);
    }
    
    @Test
    public void testAddToCart() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 TEST 4: ADD TO CART TEST");
        System.out.println("=".repeat(60));
        
        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");
        
        // Verify home page
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "❌ Home page not loaded!");
        
        // Search and add product
        SearchPage searchPage = new SearchPage(driver);
        String productName = "Sauce Labs Backpack";
        
        Assert.assertTrue(searchPage.isProductDisplayed(productName), "❌ Product not found!");
        searchPage.addToCart(productName);
        
        // Go to cart and verify
        homePage.goToCart();
        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isProductInCart(productName), "❌ Product not in cart!");
        
        System.out.println("\n✅✅✅ TEST PASSED: Product added to cart successfully! ✅✅✅\n");
        sleep(2000);
    }
    
    @Test
    public void testCompletePurchase() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 TEST 5: COMPLETE PURCHASE FLOW");
        System.out.println("=".repeat(60));
        
        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");
        
        // Add product to cart
        SearchPage searchPage = new SearchPage(driver);
        String productName = "Sauce Labs Backpack";
        searchPage.addToCart(productName);
        
        // Go to cart
        HomePage homePage = new HomePage(driver);
        homePage.goToCart();
        
        // Checkout
        CartPage cartPage = new CartPage(driver);
        cartPage.proceedToCheckout();
        
        // Enter details and finish
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterCheckoutDetails("John", "Doe", "12345");
        checkoutPage.finishCheckout();
        
        // Verify success
        Assert.assertTrue(checkoutPage.isCheckoutSuccessful(), "❌ Checkout failed!");
        
        System.out.println("\n✅✅✅ TEST PASSED: Purchase completed successfully! ✅✅✅\n");
        sleep(2000);
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}