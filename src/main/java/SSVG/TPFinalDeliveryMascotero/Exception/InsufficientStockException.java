package SSVG.TPFinalDeliveryMascotero.Exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class InsufficientStockException extends RuntimeException {

    private final Map<String, List<String>> errorsMap;

    public InsufficientStockException(Map<String, List<String>> errorsMap) {
        super("Error");
        this.errorsMap = errorsMap;
    }
}
