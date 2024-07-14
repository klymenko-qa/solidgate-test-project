package solidgate.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentPageResponse {
    private String url;
    private String guid;
    private ErrorDetails error;

    @Data
    public static class ErrorDetails {
        private String code;
        private String[] message;
    }
}
