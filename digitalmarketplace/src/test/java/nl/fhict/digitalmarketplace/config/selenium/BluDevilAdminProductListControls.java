package nl.fhict.digitalmarketplace.config.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class BluDevilAdminProductListControls {
    private SeleniumConfig config;
    private int waitTime = 2000; // wait time in ms

    public BluDevilAdminProductListControls(SeleniumConfig config) {
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

    public void editProduct(Integer productId) throws InterruptedException {
        String productElementId = "product-"+productId;
        JavascriptExecutor jse = ((JavascriptExecutor)this.config.getDriver());
        WebElement productCard = config.getDriver().findElement(By.id(productElementId));
        productCard.click();
        Thread.sleep(1000);
        String jsScript = "let btnProdEdit = document.querySelector(\"#" + productElementId + " div.product-control-btns button.btn-product-edit\");" +
                "btnProdEdit.click();";
        jse.executeScript(jsScript);
    }

    public void inspectProduct(Integer productId) throws InterruptedException {
        String productElementId = "product-"+productId;
        JavascriptExecutor jse = ((JavascriptExecutor)this.config.getDriver());
        WebElement productCard = config.getDriver().findElement(By.id(productElementId));
        productCard.click();
        Thread.sleep(1000);
        String jsScript = "let btnProdInspect = document.querySelector(\"#" + productElementId + " div.product-control-btns button.btn-product-inspect\");" +
                "btnProdInspect.click();";
        jse.executeScript(jsScript);
    }

    public void deactivateProduct(Integer productId) throws InterruptedException {
        String productElementId = "product-"+productId;
        JavascriptExecutor jse = ((JavascriptExecutor)this.config.getDriver());
        WebElement productCard = config.getDriver().findElement(By.id(productElementId));
        productCard.click();
        Thread.sleep(1000);
        String jsScript = "let btnProdState = document.querySelector(\"#" + productElementId + " div.product-control-btns button.btn-product-deactivate\");" +
                "btnProdState.click();";
        jse.executeScript(jsScript);
    }

    public boolean checkIfProductExists(Integer productId){
        String productElementId = "product-"+productId;
        try {
            WebElement productCard = config.getDriver().findElement(By.id(productElementId));
            return true;
        }catch (NoSuchElementException e){
            return false;
        }
    }

    public void viewProductsByState(boolean productsState){
        JavascriptExecutor jse = ((JavascriptExecutor)this.config.getDriver());
        String jsScript;
        if(productsState){
            jsScript = "let switchActive = " +
                    "document.querySelector(\"" +
                    "div.side-area div.side-area-inner-container div.btn-group button.btn-state-available\"" +
                    ");" +
                    "switchActive.click();";
        }
        else{
            jsScript = "let switchActive = " +
                    "document.querySelector(\"" +
                    "div.side-area div.side-area-inner-container div.btn-group button.btn-state-unavailable\"" +
                    ");" +
                    "switchActive.click();";
        }
        jse.executeScript(jsScript);
    }
}
