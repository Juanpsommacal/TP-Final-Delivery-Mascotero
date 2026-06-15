package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PagoResponseDTO {

    private Long id;

    private LocalDateTime fecha;

    private BigDecimal monto;

    private String metodoPago;

    private Long pedidoId;

    private Long clienteId;

}
