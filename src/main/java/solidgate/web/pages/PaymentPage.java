package solidgate.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class PaymentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cardNumberField = By.id("ccnumber");
    private final By cardExpiryField = By.name("cardExpiryDate");
    private final By cvvField = By.cssSelector("[data-testid='cardCvv']");
    private final By payButton = By.xpath("//button[@type='submit']");
    private final By amountCurrencyElement = By.cssSelector("[data-testid='price_major']");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='payment-method-header']")));

        PageFactory.initElements(driver, this);
    }

    public void fillInAndSubmitPayment(String cardNumber, String expiryDate, String cvv) {
        driver.findElement(cardNumberField).sendKeys(cardNumber);
        driver.findElement(cardExpiryField).sendKeys(expiryDate);
        driver.findElement(cvvField).sendKeys(cvv);
        driver.findElement(payButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-testid='payment-method-header']")));
    }

    public int getAmount() {
        String amount = driver.findElement(amountCurrencyElement).getText().replaceAll("\\D+", "");
        return Integer.parseInt(amount);
    }

    public String getCurrency() {
        return driver.findElement(amountCurrencyElement).getText().substring(0, 1);
    }
}
