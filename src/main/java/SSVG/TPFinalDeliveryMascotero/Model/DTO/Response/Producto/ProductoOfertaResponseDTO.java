package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
/// Response de Producto resumido, para mostralo dentro de una Oferta
public class ProductoOfertaResponseDTO {

    private Long id;

    private String nombre;
}
