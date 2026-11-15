package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrdenRepartoRequestDTO {

    // Los pedidos en el orden en que se van a entregar
    @NotEmpty(message = "El recorrido tiene que tener al menos un pedido")
    private List<Long> pedidoIds;
}
