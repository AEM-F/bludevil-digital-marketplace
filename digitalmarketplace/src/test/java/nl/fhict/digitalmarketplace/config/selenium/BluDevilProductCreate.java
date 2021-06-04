package nl.fhict.digitalmarketplace.config.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BluDevilProductCreate {
    private SeleniumConfig config;
    private int waitTime = 2000; // wait time in ms

    public BluDevilProductCreate(SeleniumConfig config) {
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

    public void enterProductData(String name,
                                     String price,
                                     String description,
                                     String systemReq,
                                     String imgUrl,
                                     List<Integer> genreIds,
                                 String releaseDate) throws InterruptedException {
        WebElement nameInput = config.getDriver().findElement(By.id("formControlInput1"));
        nameInput.sendKeys(name);
        Thread.sleep(500);
        WebElement priceInput = config.getDriver().findElement(By.id("formControlInput2"));
        priceInput.sendKeys(price);
        Thread.sleep(500);
        WebElement descriptionInput = config.getDriver().findElement(By.id("formControlTextarea1"));
        descriptionInput.sendKeys(description);
        Thread.sleep(500);
        WebElement systemReqInput = config.getDriver().findElement(By.id("formControlTextarea2"));
        systemReqInput.sendKeys(systemReq);
        Thread.sleep(500);
        WebElement releaseDateInput = config.getDriver().findElement(By.id("formControlInput4"));
        releaseDateInput.sendKeys(releaseDate);
        Thread.sleep(500);

        WebElement imageUrlBtn = config.getDriver().findElement(By.id("v-pills-link-tab"));
        imageUrlBtn.click();
        Thread.sleep(1000);
        WebElement imgUrlInput = config.getDriver().findElement(By.id("formControlInput3"));
        imgUrlInput.sendKeys(imgUrl);
        Thread.sleep(500);

        for (Integer genreId:genreIds) {
            String genreFullId = "genre-"+genreId;
            WebElement genre = config.getDriver().findElement(By.id(genreFullId));
            genre.click();
            Thread.sleep(500);
        }
    }

    public void submitCreateForm() throws InterruptedException {
        JavascriptExecutor jse = ((JavascriptExecutor)this.config.getDriver());
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(1000);
        String jsScript = "let btnFormSubmit = document.querySelector(\"div.btn-main-group button[type='submit']\");" +
                "btnFormSubmit.click();";
        jse.executeScript(jsScript);
    }
}
