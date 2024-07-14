package solidgate.models;

import lombok.Data;

@Data
public class PageCustomization {
    private String public_name;
    private String order_title;
    private String order_description;
    private String[] payment_methods;
    private String button_font_color;
    private String button_color;
    private String font_name;
    private boolean is_cardholder_visible;
    private String terms_url;
    private String back_url;

    public static PageCustomization getDefaultPageCustomization() {
        PageCustomization pageCustomization = new PageCustomization();
        pageCustomization.setPublic_name("Public Name");
        pageCustomization.setOrder_title("Order Title");
        pageCustomization.setOrder_description("Premium package");
        pageCustomization.setPayment_methods(new String[]{"paypal"});
        pageCustomization.setButton_font_color("#FFFFFF");
        pageCustomization.setButton_color("#00816A");
        pageCustomization.setFont_name("Open Sans");
        pageCustomization.set_cardholder_visible(true);
        pageCustomization.setTerms_url("https://solidgate.com/terms");
        pageCustomization.setBack_url("https://solidgate.com");

        return pageCustomization;
    }
}
