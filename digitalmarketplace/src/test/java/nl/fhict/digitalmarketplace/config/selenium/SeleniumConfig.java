package nl.fhict.digitalmarketplace.config.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumConfig {
    private WebDriver driver;

    public SeleniumConfig() {
        driver = new FirefoxDriver();
    }

    static {
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver\\geckodriver.exe");
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
