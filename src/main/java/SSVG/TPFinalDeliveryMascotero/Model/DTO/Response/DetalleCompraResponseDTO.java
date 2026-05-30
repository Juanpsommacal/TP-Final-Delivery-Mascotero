package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DetalleCompraResponseDTO {

    private String nombreProducto;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal precioTotal;
}
