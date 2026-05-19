package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import lombok.Setter;

@Setter
public class AntipulgasResponseDTO {

    private String marca;

    private String nombre;

    private Double kgMin;

    private Double kgMax;

    private String descripcion;

    private Double precio;

    private Integer stock;

    private TipoAnimal tipoAnimal;

    private TipoAntipulgas tipoAntipulgas;
}
