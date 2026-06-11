package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pago;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.MetodoPago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagoCreateRequestDTO {

    @NotNull(message = "El monto no puede estar vacio")
    @Positive(message = "El monto debe ser un valor positivo")
    private BigDecimal monto;

    @NotNull(message = "El metodo de pago no puede estar vacio")
    private MetodoPago metodoPago;

    @NotNull(message = "La id del pedido no puede estar vacia")
    private Long pedidoId;

    @NotNull(message = "La id del cliente no puede estar vacia")
    private Long clienteId;

}
