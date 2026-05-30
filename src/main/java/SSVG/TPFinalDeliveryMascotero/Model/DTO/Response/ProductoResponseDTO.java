package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductoResponseDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private Integer stock;

    private String marca;

    private Boolean activo;

    private String tipoProducto;

}
