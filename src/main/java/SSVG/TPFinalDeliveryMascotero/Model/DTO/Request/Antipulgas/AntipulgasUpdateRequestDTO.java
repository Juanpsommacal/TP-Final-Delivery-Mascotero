package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Producto.ProductoUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import SSVG.TPFinalDeliveryMascotero.Validations.ValidEnum;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Los campos comunes (nombre, descripcion, precio, stock y marca) vienen de ProductoUpdateRequestDTO
@Getter
@Setter
@NoArgsConstructor
public class AntipulgasUpdateRequestDTO extends ProductoUpdateRequestDTO {

    @Size(max = 10, message = "El tipo de animal no puede exceder los 10 caracteres")
    @ValidEnum(enumClass = TipoAnimal.class, message = "El tipo de animal debe ser GATO o PERRO")
    private String tipoAnimal;

    @PositiveOrZero(message = "El peso minimo no puede ser menor a 0")
    @Digits(integer = 2, fraction = 2, message = "El peso minimo no puede tener mas de 2 digitos")
    private Double kgMin;

    @PositiveOrZero(message = "El peso maximo no puede ser menor a 0")
    @Digits(integer = 2, fraction = 2, message = "El peso maximo no puede tener mas de 2 digitos")
    private Double kgMax;

    @Size(max = 25, message = "El tipo de antipulgas no puede exceder los 25 caracteres")
    @ValidEnum(enumClass = TipoAntipulgas.class, message = "El tipo de antipulgas debe ser PIPETA o COMPRIMIDO")
    private String tipoAntipulgas;
}
