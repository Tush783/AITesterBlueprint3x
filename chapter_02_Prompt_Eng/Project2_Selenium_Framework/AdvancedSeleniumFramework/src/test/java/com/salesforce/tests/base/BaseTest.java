package com.salesforce.tests.base;

import com.salesforce.framework.utils.DriverManager;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class BaseTest {

    protected static final String LOGIN_URL = "https://login.salesforce.com/?locale=in";
    protected static final String VALID_USERNAME = "your-valid-email@yourdomain.com";
    protected static final String VALID_PASSWORD = "YourValidPassword";
    protected static final String INVALID_USERNAME = "invalid.user@test.com";
    protected static final String INVALID_PASSWORD = "WrongPassword@999";

    @Parameters("browser")
    @BeforeTest
    public void setUp(String browser) {
        DriverManager.initDriver(browser);
    }

    @AfterTest
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
