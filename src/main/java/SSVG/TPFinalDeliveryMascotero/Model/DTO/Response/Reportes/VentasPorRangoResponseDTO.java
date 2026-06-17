package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VentasPorRangoResponseDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long cantidadPedidos;
    private BigDecimal totalVentas;
}
