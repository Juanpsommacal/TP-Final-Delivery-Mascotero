package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PagoResponseDTO {

    private LocalDate fecha;

    private BigDecimal monto;

    private String metodoPago;



}
