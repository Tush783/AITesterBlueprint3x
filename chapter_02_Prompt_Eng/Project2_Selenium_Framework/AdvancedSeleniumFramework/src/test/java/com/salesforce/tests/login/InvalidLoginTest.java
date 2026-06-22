package com.salesforce.tests.login;

import com.salesforce.framework.pages.LoginPage;
import com.salesforce.framework.utils.DriverManager;
import com.salesforce.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InvalidLoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.navigateTo(LOGIN_URL);
    }

    @Test(description = "Verify error message is shown with invalid username and password")
    public void testInvalidCredentials() throws Exception {
        try {
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page was not displayed");
            loginPage.doLogin(INVALID_USERNAME, INVALID_PASSWORD);
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message was not displayed for invalid credentials");
            Assert.assertFalse(loginPage.getErrorMessage().isEmpty(), "Error message text was empty");
        } catch (AssertionError ae) {
            throw new AssertionError("testInvalidCredentials assertion failed: " + ae.getMessage(), ae);
        } catch (Exception e) {
            throw new Exception("testInvalidCredentials encountered an unexpected error: " + e.getMessage(), e);
        }
    }

    @Test(description = "Verify error message is shown when username is empty")
    public void testEmptyUsername() throws Exception {
        try {
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page was not displayed");
            loginPage.doLogin("", VALID_PASSWORD);
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message was not displayed for empty username");
        } catch (AssertionError ae) {
            throw new AssertionError("testEmptyUsername assertion failed: " + ae.getMessage(), ae);
        } catch (Exception e) {
            throw new Exception("testEmptyUsername encountered an unexpected error: " + e.getMessage(), e);
        }
    }

    @Test(description = "Verify error message is shown when password is empty")
    public void testEmptyPassword() throws Exception {
        try {
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page was not displayed");
            loginPage.doLogin(VALID_USERNAME, "");
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message was not displayed for empty password");
        } catch (AssertionError ae) {
            throw new AssertionError("testEmptyPassword assertion failed: " + ae.getMessage(), ae);
        } catch (Exception e) {
            throw new Exception("testEmptyPassword encountered an unexpected error: " + e.getMessage(), e);
        }
    }

    @Test(description = "Verify error message is shown when both username and password are empty")
    public void testBothFieldsEmpty() throws Exception {
        try {
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page was not displayed");
            loginPage.doLogin("", "");
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message was not displayed when both fields are empty");
        } catch (AssertionError ae) {
            throw new AssertionError("testBothFieldsEmpty assertion failed: " + ae.getMessage(), ae);
        } catch (Exception e) {
            throw new Exception("testBothFieldsEmpty encountered an unexpected error: " + e.getMessage(), e);
        }
    }
}
