package nl.fhict.digitalmarketplace.config.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class BluDevilSeleniumNavMenu {
    private SeleniumConfig config;
    private int waitTime = 1000; // wait time in ms

    public BluDevilSeleniumNavMenu(SeleniumConfig config) {
        this.config = config;
    }

    public void closeWindow() {
        this.config.getDriver().close();
    }

    public void closeBrowser() {
        this.config.getDriver().quit();
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void openNavMenu() {
        WebElement openBtn = config.getDriver().findElement(By.id("menu-btn"));
        openBtn.click();
    }

    public void closeNavMenu(){
        WebElement closeBtn = config.getDriver().findElement(By.id("close-menu-btn"));
        closeBtn.click();
    }

    public void logout() {
        WebElement logoutBtn = config.getDriver().findElement(By.id("logout-btn"));
        logoutBtn.click();
    }

    public void navigateTo(ENavOption navOption) throws InterruptedException {
        String elementId = "";
        switch (navOption) {
            case HOME: {
                elementId = "nav-home";
                break;
            }
            case LOGIN: {
                elementId = "nav-login";
                break;
            }
            case CATALOG: {
                elementId = "nav-catalog";
                break;
            }
            case PRODUCT_CREATION: {
                WebElement prodCollapse = config.getDriver().findElement(By.id("product-section-collapse"));
                prodCollapse.click();
                Thread.sleep(waitTime);
                elementId = "nav-product-create";
                break;
            }
            case PRODUCT_MANAGE: {
                WebElement prodCollapse = config.getDriver().findElement(By.id("product-section-collapse"));
                prodCollapse.click();
                Thread.sleep(waitTime);
                elementId = "nav-product-manage";
                break;
            }
        }
        WebElement navElement = config.getDriver().findElement(By.id(elementId));
        navElement.click();
    }
}
