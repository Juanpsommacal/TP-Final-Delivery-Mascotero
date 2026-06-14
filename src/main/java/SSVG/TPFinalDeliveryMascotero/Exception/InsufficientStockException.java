package SSVG.TPFinalDeliveryMascotero.Exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class InsufficientStockException extends RuntimeException {

    private final Map<String, String> errorsMap;

    public InsufficientStockException(Map<String, String> errorsMap) {
        super("Error");
        this.errorsMap = errorsMap;
    }
}
