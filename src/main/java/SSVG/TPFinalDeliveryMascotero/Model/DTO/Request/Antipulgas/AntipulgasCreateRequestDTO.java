package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import SSVG.TPFinalDeliveryMascotero.Validations.ValidEnum;
import jakarta.validation.constraints.*;
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
    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    private String nombre;

    @Size(max = 100, message = "La descripcion no puede exceder los 100 caracteres")
    private String descripcion;

    @NotNull(message = "El precio no puede estar vacio")
    @PositiveOrZero(message = "El precio no puede ser menor a 0")
    @Digits(integer = 7, fraction = 2, message = "El precio no puede tener mas de 7 digitos")
    private BigDecimal precio;

    @NotNull(message = "El stock no puede estar vacio")
    @PositiveOrZero(message = "El stock no puede ser menor a 0")
    @Digits(integer = 3, fraction = 0, message = "El stock no puede tener mas de 3 digitos")
    private Integer stock;

    @NotBlank(message = "La marca no puede estar vacia")
    @Size(max = 20, message = "La marca no puede exceder los 20 caracteres")
    private String marca;

    @NotNull(message = "El tipo de animal no puede estar vacio")
    @Size(max = 10, message = "El tipo de animal no puede exceder los 10 caracteres")
    @ValidEnum(enumClass = TipoAnimal.class, message = "El tipo de animal debe ser GATO o PERRO")
    private String tipoAnimal;

    @PositiveOrZero(message = "El peso minimo no puede ser menor a 0")
    @NotNull(message = "El peso minimo no puede estar vacio")
    @Digits(integer = 2, fraction = 2, message = "El peso minimo no puede tener mas de 2 digitos")
    private Double kgMin;

    @PositiveOrZero(message = "El peso maximo no puede ser menor a 0")
    @NotNull(message = "El peso maximo no puede estar vacio")
    @Digits(integer = 2, fraction = 2, message = "El peso maximo no puede tener mas de 2 digitos")
    private Double kgMax;

    @NotNull(message = "El tipo de antipulgas no puede estar vacio")
    @Size(max = 25, message = "El tipo de antipulgas no puede exceder los 25 caracteres")
    @ValidEnum(enumClass = TipoAntipulgas.class, message = "El tipo de antipulgas debe ser PIPETA o COMPRIMIDO")
    private String tipoAntipulgas;
}
