package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void testValidLogin() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 TEST 1: VALID LOGIN TEST");
        System.out.println("=".repeat(60));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        HomePage homePage = new HomePage(driver);
        boolean isLoggedIn = homePage.isHomePageDisplayed();

        Assert.assertTrue(isLoggedIn, "❌ Login failed!");

        System.out.println("\n✅✅✅ TEST PASSED: Valid Login Successful! ✅✅✅\n");
        sleep(2000);
    }

    @Test
    public void testInvalidLogin() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 TEST 2: INVALID LOGIN TEST");
        System.out.println("=".repeat(60));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("wrong_user", "wrong_pass");

        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Epic sadface"), "❌ Error message not displayed!");

        System.out.println("📧 Error message: " + errorMsg);
        System.out.println("\n✅✅✅ TEST PASSED: Invalid Login Handled Correctly! ✅✅✅\n");
        sleep(2000);
    }
}