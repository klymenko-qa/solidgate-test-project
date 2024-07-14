package solidgate.models;

import lombok.Data;

@Data
public class PaymentPageRequest {
    private Order order;
    private PageCustomization page_customization;

    public static PaymentPageRequest getRandomPaymentPageRequest() {
        PaymentPageRequest paymentPageRequest = new PaymentPageRequest();
        paymentPageRequest.setOrder(Order.getDefaultOrder());
        paymentPageRequest.setPage_customization(PageCustomization.getDefaultPageCustomization());

        return paymentPageRequest;
    }
}
