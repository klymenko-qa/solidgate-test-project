package solidgate.api;

import solidgate.models.OrderStatusRequest;
import solidgate.models.PaymentPageRequest;
import solidgate.models.PaymentPageResponse;
import solidgate.utils.SignatureGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SolidgateApiClient {

    private final String publicKey;
    private final String secretKey;

    public SolidgateApiClient(String publicKey, String secretKey) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public PaymentPageResponse createPaymentPage(PaymentPageRequest paymentPageRequest) throws Exception {
        RestAssured.baseURI = "https://payment-page.solidgate.com/api/v1";

        String jsonString = new ObjectMapper().writeValueAsString(paymentPageRequest);
        String signature = SignatureGenerator.generateSignature(publicKey, jsonString, secretKey);

        Response response = given()
                .contentType("application/json")
                .header("merchant", publicKey)
                .header("signature", signature)
                .body(paymentPageRequest)
                .when()
                .post("/init")
                .then()
                .extract().response();

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to create payment page: " + response.getStatusLine());
        }

        PaymentPageResponse paymentPageResponse = new ObjectMapper().readValue(response.asString(), PaymentPageResponse.class);

        if (paymentPageResponse.getError() != null) {
            throw new RuntimeException("Error from API: " + paymentPageResponse.getError().getCode() + " - " + String.join(", ", paymentPageResponse.getError().getMessage()));
        }

        return paymentPageResponse;
    }

    public JsonNode checkOrderStatus(String orderId) throws Exception {
        RestAssured.baseURI = "https://pay.solidgate.com/api/v1";

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
        orderStatusRequest.setOrder_id(orderId);

        String jsonString = new ObjectMapper().writeValueAsString(orderStatusRequest);
        String signature = SignatureGenerator.generateSignature(publicKey, jsonString, secretKey);

        Response response = given()
                .contentType("application/json")
                .header("merchant", publicKey)
                .header("signature", signature)
                .body(orderStatusRequest)
                .when()
                .post("/status")
                .then()
                .extract().response();

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to check Order status: " + response.getStatusLine());
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.asString());
    }
}
