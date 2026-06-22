package com.framework.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.framework.config.ConfigManager;
import com.framework.utils.ExtentReportManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    protected static RequestSpecification requestSpec;

    protected static final ExtentReports extent = ExtentReportManager.getInstance();

    // One ExtentTest per thread so parallel execution is safe
    protected static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        ConfigManager config = ConfigManager.getInstance();

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter());

        applyAuth(builder, config);

        requestSpec = builder.build();
        RestAssured.requestSpecification = requestSpec;
    }

    private void applyAuth(RequestSpecBuilder builder, ConfigManager config) {
        String authType  = config.getAuthType();
        String authToken = config.getAuthToken();

        if (authType == null || authType.isBlank()
                || authToken == null || authToken.isBlank()) {
            return;
        }

        switch (authType.trim().toLowerCase()) {
            case "bearer" -> builder.addHeader("Authorization", "Bearer " + authToken);
            case "apikey" -> builder.addHeader("x-api-key", authToken);
            case "basic"  -> builder.addHeader("Authorization", "Basic "  + authToken);
            default       -> throw new IllegalArgumentException(
                    "Unsupported auth.type: " + authType);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void recordTestResult(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test == null) return;

        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> test.pass("PASS");
            case ITestResult.FAILURE -> test.fail(result.getThrowable());
            case ITestResult.SKIP    -> test.skip(result.getThrowable());
        }
        extentTest.remove();
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        extent.flush();
    }
}
