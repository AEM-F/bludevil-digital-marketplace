package nl.fhict.digitalmarketplace.config.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class BluDevilSeleniumLogin {
    private SeleniumConfig config;
    private String url = "http://localhost:8080/#/login";
    private int waitTime = 2000; // wait time in ms

    public BluDevilSeleniumLogin() {
        config = new SeleniumConfig();
        config.getDriver().manage().window().maximize();
        config.getDriver().get(url);
    }

    public String getCurrentUrl(){
        return config.getDriver().getCurrentUrl();
    }

    public SeleniumConfig getConfig() {
        return config;
    }

    public void closeWindow(){
        this.config.getDriver().close();
    }

    public void closeBrowser(){
        this.config.getDriver().quit();
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void typeUserCredentialsLogin(String email, String password){
        WebElement loginField = config.getDriver().findElement(By.id("username"));
        WebElement passwordField = config.getDriver().findElement(By.id("password"));
        loginField.sendKeys(email);
        passwordField.sendKeys(password);
    }

    public void submitLoginForm(){
        WebElement submitBtn = config.getDriver().findElement(By.id("login-btn"));
        submitBtn.click();
    }

    public void navigateToSignup(){
        WebElement signupSwitchBtn = config.getDriver().findElement(By.id("pills-signup-tab"));
        signupSwitchBtn.click();
    }

    public void typeUserCredentialsSignup(String email, String password, String confirmPassword){
        WebElement emailField = config.getDriver().findElement(By.id("su-email"));
        WebElement passwordField = config.getDriver().findElement(By.id("su-password"));
        WebElement confirmPasswordField = config.getDriver().findElement(By.id("su-confirmPassword"));
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        confirmPasswordField.sendKeys(confirmPassword);
    }

    public void submitSignupForm(){
        WebElement signupSubmitBtn = config.getDriver().findElement(By.id("signup-btn"));
        signupSubmitBtn.click();
    }
}
