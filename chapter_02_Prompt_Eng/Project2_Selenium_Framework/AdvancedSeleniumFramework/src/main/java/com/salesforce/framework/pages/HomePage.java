package com.salesforce.framework.pages;

import com.salesforce.framework.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//div[contains(@class,'slds-global-header')]")
    private WebElement globalHeader;

    @FindBy(xpath = "//button[@title='View profile']")
    private WebElement userProfileButton;

    @FindBy(xpath = "//div[contains(@class,'navContainer')]")
    private WebElement navigationContainer;

    @FindBy(xpath = "//span[contains(@class,'avatar')]//img")
    private WebElement userAvatar;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = DriverManager.getWait();
        PageFactory.initElements(driver, this);
    }

    public boolean isHomePageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(globalHeader));
            return globalHeader.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOf(userProfileButton));
            return userProfileButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
