package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, List<String>> errorsMap;

    public ErrorResponseDTO(LocalDateTime timestamp, int status, String error, String message){
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
