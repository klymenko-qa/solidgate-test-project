package solidgate.models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class Order {
    private String order_id;
    private int amount;
    private String currency;
    private String order_description;
    private String order_items;
    private String order_date;
    private int order_number;
    private String type;
    private int settle_interval;
    private int retry_attempt;
    private boolean force3ds;
    private String[] google_pay_allowed_auth_methods;
    private String customer_date_of_birth;
    private String customer_email;
    private String customer_first_name;
    private String customer_last_name;
    private String customer_phone;
    private String traffic_source;
    private String transaction_source;
    private String purchase_country;
    private String geo_country;
    private String geo_city;
    private String language;
    private String website;
    private Map<String, String> order_metadata;
    private String success_url;
    private String fail_url;

    public static Order getDefaultOrder() {
        Order order = new Order();
        order.setOrder_id(UUID.randomUUID().toString());
        order.setAmount(123500);
        order.setCurrency("EUR");
        order.setOrder_description("Premium package");
        order.setOrder_items("item 1 x 10, item 2 x 30");
        order.setOrder_date("2015-12-21 11:21:30");
        order.setOrder_number(9);
        order.setType("auth");
        order.setSettle_interval(0);
        order.setRetry_attempt(3);
        order.setForce3ds(false);
        order.setGoogle_pay_allowed_auth_methods(new String[]{"PAN_ONLY"});
        order.setCustomer_date_of_birth("1988-11-21");
        order.setCustomer_email("example@example.com");
        order.setCustomer_first_name("Nikola");
        order.setCustomer_last_name("Tesla");
        order.setCustomer_phone("+10111111111");
        order.setTraffic_source("facebook");
        order.setTransaction_source("main_menu");
        order.setPurchase_country("USA");
        order.setGeo_country("USA");
        order.setGeo_city("New Castle");
        order.setLanguage("pt");
        order.setWebsite("https://solidgate.com");
        Map<String, String> orderMetadata = new HashMap<>();
        orderMetadata.put("coupon_code", "NY2018");
        orderMetadata.put("partner_id", "123989");
        order.setOrder_metadata(orderMetadata);
        order.setSuccess_url("https://ruperhat.com/payment/");
        order.setFail_url("http://merchant.example/fail");

        return order;
    }
}
