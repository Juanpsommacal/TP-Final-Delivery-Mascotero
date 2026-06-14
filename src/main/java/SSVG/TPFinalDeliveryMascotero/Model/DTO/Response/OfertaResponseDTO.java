package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoOfertaResponseDTO;
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
public class OfertaResponseDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private Double porcentaje;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private List<ProductoOfertaResponseDTO> productos;


}
