package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CompraResponseDTO {

    private String proveedor;

    private LocalDate fecha;

    private String estado;

    private BigDecimal montoTotal;
}
