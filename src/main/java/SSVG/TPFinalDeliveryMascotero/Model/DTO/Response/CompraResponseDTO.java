package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompraResponseDTO {

    private LocalDate fecha;

    private String nombreProveedor;

    private String estadoCompra;

    private List<DetalleCompraResponseDTO> detalleResponseDTO;

    private BigDecimal montoTotal;
}
