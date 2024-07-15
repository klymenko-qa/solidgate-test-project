package solidgate.tests;

import solidgate.api.SolidgateApiClient;
import solidgate.models.PaymentPageRequest;
import solidgate.models.PaymentPageResponse;
import solidgate.web.pages.PaymentPage;
import solidgate.web.driver.DriverFactory;
import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

public class PaymentTest {

    private WebDriver driver;
    private Assertion assertion;
    private SolidgateApiClient apiClient;
    private String orderId;
    private int expectedAmount;
    private String expectedCurrency;
    private static final Logger logger = LoggerFactory.getLogger(PaymentTest.class);

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createChromeDriver();
        String publicKey = "api_pk_c999f213_a26d_490d_993f_4e2bc848763c";
        String secretKey = "api_sk_346b1fb4_c18f_4fc1_a800_5f6e4256c9a1";
        apiClient = new SolidgateApiClient(publicKey, secretKey);
        assertion = new Assertion();
    }

    @Test(priority = 1)
    public void createPaymentPageAndPerformPaymentTest() throws Exception {
        // Create Payment page
        PaymentPageRequest paymentPageRequest = PaymentPageRequest.getRandomPaymentPageRequest();
        orderId = paymentPageRequest.getOrder().getOrder_id();

        PaymentPageResponse paymentPageResponse = apiClient.createPaymentPage(paymentPageRequest);

        String paymentPageUrl = paymentPageResponse.getUrl();
        logger.info("Payment Page URL: {}", paymentPageUrl);
        logger.info("Order Id: {}", orderId);

        // Navigate to the created page
        driver.get(paymentPageUrl);

        PaymentPage paymentPage = new PaymentPage(driver);

        // Get amount and currency for further validation
        expectedAmount = paymentPage.getAmount();
        expectedCurrency = paymentPage.getCurrency();

        // Fill in payment card properties
        String cardNumber = "4067429974719265";
        String expiryDate = "12/25";
        String cvv = "123";
        paymentPage.fillInAndSubmitPayment(cardNumber, expiryDate, cvv);

        // Check that payment was successful
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = paymentPageRequest.getOrder().getSuccess_url();
        assertion.assertEquals(currentUrl, expectedUrl, "Success URL does not match");
    }

    @Test(priority = 2, dependsOnMethods = "createPaymentPageAndPerformPaymentTest")
    public void checkOrderStatusTest() throws Exception {
        // Check Order status and properties
        JsonNode orderStatus = apiClient.checkOrderStatus(orderId);

        int actualAmount = orderStatus.path("order").path("amount").asInt();
        String actualCurrency = orderStatus.path("order").path("currency").asText();
        if (actualCurrency.equals("EUR")) {
            actualCurrency = "€";
        } else {
            logger.info("Unknown currency");
        }
        String actualStatus = orderStatus.path("order").path("status").asText();

        assertion.assertEquals(actualAmount, expectedAmount, "Amount does not match");
        assertion.assertEquals(actualCurrency, expectedCurrency, "Currency does not match");
        assertion.assertEquals(actualStatus, "auth_ok", "Status does not match");

        logger.info("Order status check passed: amount = {}, currency = {}, status = {}", actualAmount, actualCurrency, actualStatus);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
