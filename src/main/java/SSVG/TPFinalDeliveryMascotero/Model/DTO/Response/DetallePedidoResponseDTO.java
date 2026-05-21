package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Setter;

import java.math.BigDecimal;

@Setter
public class DetallePedidoResponseDTO {

    private String nombreProducto;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal subtotal;

    private Double descuentoAplicado;

    private BigDecimal precioTotal;


}
