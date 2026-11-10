package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DetallePedidoResponseDTO {

    // Para poder repetir un pedido desde el front sin depender del nombre
    private Long productoId;

    private String nombreProducto;

    private Integer cantidad;

    private Integer stockDisponible;

    private BigDecimal precioUnitario;

    private Double descuentoAplicado;

    private BigDecimal precioDescuento;

    private BigDecimal subtotal;

}
