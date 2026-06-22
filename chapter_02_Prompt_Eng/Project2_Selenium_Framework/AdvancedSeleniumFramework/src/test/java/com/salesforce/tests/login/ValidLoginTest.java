package com.salesforce.tests.login;

import com.salesforce.framework.pages.HomePage;
import com.salesforce.framework.pages.LoginPage;
import com.salesforce.framework.utils.DriverManager;
import com.salesforce.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ValidLoginTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(DriverManager.getDriver());
        homePage = new HomePage(DriverManager.getDriver());
        loginPage.navigateTo(LOGIN_URL);
    }

    @Test(description = "Verify successful login with valid credentials")
    public void testValidLogin() throws Exception {
        try {
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page was not displayed");
            loginPage.doLogin(VALID_USERNAME, VALID_PASSWORD);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page did not load after valid login");
            Assert.assertTrue(homePage.getCurrentUrl().contains("salesforce.com"), "URL does not contain salesforce.com after login");
        } catch (AssertionError ae) {
            throw new AssertionError("testValidLogin assertion failed: " + ae.getMessage(), ae);
        } catch (Exception e) {
            throw new Exception("testValidLogin encountered an unexpected error: " + e.getMessage(), e);
        }
    }

    @Test(description = "Verify successful login with Remember Me checked")
    public void testValidLoginWithRememberMe() throws Exception {
        try {
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page was not displayed");
            loginPage.doLoginWithRememberMe(VALID_USERNAME, VALID_PASSWORD);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page did not load after valid login with Remember Me");
            Assert.assertTrue(homePage.getCurrentUrl().contains("salesforce.com"), "URL does not contain salesforce.com after login with Remember Me");
        } catch (AssertionError ae) {
            throw new AssertionError("testValidLoginWithRememberMe assertion failed: " + ae.getMessage(), ae);
        } catch (Exception e) {
            throw new Exception("testValidLoginWithRememberMe encountered an unexpected error: " + e.getMessage(), e);
        }
    }
}
