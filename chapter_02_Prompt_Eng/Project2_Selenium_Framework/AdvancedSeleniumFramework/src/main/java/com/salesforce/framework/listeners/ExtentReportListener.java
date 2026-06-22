package com.salesforce.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.salesforce.framework.utils.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    private static synchronized ExtentReports buildExtentReports() {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        spark.config().setDocumentTitle("Salesforce Automation Report");
        spark.config().setReportName("Login Test Suite");
        spark.config().setTheme(Theme.DARK);
        spark.config().setEncoding("UTF-8");

        ExtentReports reports = new ExtentReports();
        reports.attachReporter(spark);
        reports.setSystemInfo("Application", "Salesforce CRM");
        reports.setSystemInfo("Environment", "Production");
        reports.setSystemInfo("OS", System.getProperty("os.name"));
        reports.setSystemInfo("Java Version", System.getProperty("java.version"));
        return reports;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        extent = buildExtentReports();
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getDescription().isEmpty()
                ? result.getName()
                : result.getMethod().getDescription();
        ExtentTest test = extent.createTest(testName);
        testThreadLocal.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThreadLocal.get().log(Status.PASS, "Test passed: " + result.getName());
        testThreadLocal.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = testThreadLocal.get();
        test.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
        try {
            String base64Screenshot = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BASE64);
            test.addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
        } catch (Exception e) {
            test.log(Status.WARNING, "Screenshot capture failed: " + e.getMessage());
        }
        testThreadLocal.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThreadLocal.get().log(Status.SKIP, "Test skipped: " + result.getName());
        testThreadLocal.remove();
    }
}
