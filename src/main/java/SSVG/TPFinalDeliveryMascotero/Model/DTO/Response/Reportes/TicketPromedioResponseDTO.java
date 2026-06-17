package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketPromedioResponseDTO {

    private Integer anio;
    private Integer mes;
    private Long cantidadPedidos;
    private BigDecimal totalVentas;
    private BigDecimal ticketPromedio;

    public TicketPromedioResponseDTO(Integer anio, Integer mes, Long cantidadPedidos, BigDecimal totalVentas) {
        this.anio = anio;
        this.mes = mes;
        this.cantidadPedidos = cantidadPedidos != null ? cantidadPedidos : 0L;
        this.totalVentas = totalVentas != null ? totalVentas : BigDecimal.ZERO;

        if (this.cantidadPedidos > 0) {
            this.ticketPromedio = this.totalVentas.divide(
                    BigDecimal.valueOf(this.cantidadPedidos),
                    2,
                    RoundingMode.HALF_UP
            );
        } else {
            this.ticketPromedio = BigDecimal.ZERO;
        }
    }


}
