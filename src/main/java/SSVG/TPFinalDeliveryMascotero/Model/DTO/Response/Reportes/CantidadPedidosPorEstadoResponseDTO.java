package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CantidadPedidosPorEstadoResponseDTO {

    private EstadoPedido estado;
    private Long cantidad;
}
