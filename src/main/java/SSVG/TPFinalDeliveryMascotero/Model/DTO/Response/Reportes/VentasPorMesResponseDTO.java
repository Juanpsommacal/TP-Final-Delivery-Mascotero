package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VentasPorMesResponseDTO {

    private Integer anio;
    private Integer mes;
    private Long cantidadPedidos;
    private BigDecimal montoTotal;


}
