package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class AntipulgasCreateRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio no puede estar vacio")
    @PositiveOrZero(message = "El precio no puede ser menor a 0")
    private BigDecimal precio;

    @NotNull(message = "El stock no puede estar vacio")
    @PositiveOrZero(message = "El stock no puede ser menor a 0")
    private Integer stock;

    @NotBlank(message = "La marca no puede estar vacia")
    private String marca;

    @NotNull(message = "El tipo de animal no puede estar vacio")
    private TipoAnimal tipoAnimal;

    @PositiveOrZero(message = "El peso minimo no puede ser menor a 0")
    @NotNull(message = "El peso minimo no puede estar vacio")
    private Double kgMin;

    @PositiveOrZero(message = "El peso maximo no puede ser menor a 0")
    @NotNull(message = "El peso maximo no puede estar vacio")
    private Double kgMax;

    @NotNull(message = "El tipo de antipulgas no puede estar vacio")
    private TipoAntipulgas tipoAntipulgas;
}
