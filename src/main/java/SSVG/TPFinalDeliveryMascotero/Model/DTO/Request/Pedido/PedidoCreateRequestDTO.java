package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.DetallePedido.DetallePedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pago.PagoCreateRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PedidoCreateRequestDTO {

    @NotNull(message = "El cliente no puede estar vacio")
    private Long clienteId;

    @NotNull(message = "La direccion no puede estar vacia")
    @Valid
    private DireccionCreateRequestDTO direccion;

    @NotEmpty(message = "El detalle del pedido no puede estar vacio")
    @Valid
    private List<DetallePedidoCreateRequestDTO> detalles;
}
