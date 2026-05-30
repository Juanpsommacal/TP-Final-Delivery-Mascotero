package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class AntipulgasResponseDTO {

    private Long id;

    private String marca;

    private String nombre;

    private Double kgMin;

    private Double kgMax;

    private String descripcion;

    private BigDecimal precio;

    private Integer stock;

    private TipoAnimal tipoAnimal;

    private TipoAntipulgas tipoAntipulgas;
}
