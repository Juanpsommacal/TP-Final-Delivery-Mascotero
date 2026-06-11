package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AntipulgasUpdateRequestDTO {

    private String nombre;

    private String descripcion;

    @PositiveOrZero(message = "El precio no debe ser menor a 0")
    private BigDecimal precio;

    @PositiveOrZero(message = "La cantidad del Stock no debe ser menor a 0")
    private Integer stock;

    private String marca;

    private TipoAnimal tipoAnimal;

    @PositiveOrZero(message = "El peso minimo admitido debe ser superior a 0")
    private Double kgMin;

    @PositiveOrZero(message = "El peso maximo admitido debe ser superior a 0")
    private Double kgMax;

    private TipoAntipulgas tipoAntipulgas;
}
