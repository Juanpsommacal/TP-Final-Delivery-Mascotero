package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMasVendidoResponseDTO {

    private Long productoId;
    private String nombreProducto;
    private Long cantidadVendida;
}
