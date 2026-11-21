package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Producto.ProductoUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
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
public class AlimentoUpdateRequestDTO extends ProductoUpdateRequestDTO {

    @PositiveOrZero(message = "El peso no puede ser menor a 0")
    @Digits(integer = 3, fraction = 1, message = "El peso no puede tener mas de 3 digitos enteros y 1 decimal")
    private Double peso;

    @Size(max = 15, message = "La unidad de medida no puede exceder los 15 caracteres")
    @ValidEnum(enumClass = UnidadMedida.class, message = "La unidad de medida debe ser GRAMOS o KILOGRAMOS")
    private String unidadMedida;

    @Size(max = 10, message = "La etapa de vida no puede exceder los 10 caracteres")
    @ValidEnum(enumClass = EtapaVida.class, message = "La etapa de vida debe ser CACHORRO, ADULTO o GERONTE")
    private String etapaVida;

    @Size(max = 10, message = "El tipo de animal no puede exceder los 10 caracteres")
    @ValidEnum(enumClass = TipoAnimal.class, message = "El tipo de animal debe ser GATO o PERRO")
    private String tipoAnimal;
}
