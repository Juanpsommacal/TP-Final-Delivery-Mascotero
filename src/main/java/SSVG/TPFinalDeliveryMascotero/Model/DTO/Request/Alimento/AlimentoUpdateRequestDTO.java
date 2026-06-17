package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
import SSVG.TPFinalDeliveryMascotero.Validations.ValidEnum;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlimentoUpdateRequestDTO {

    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    private String nombre;

    @Size(max = 100, message = "La descripcion no puede exceder los 100 caracteres")
    private String descripcion;

    @PositiveOrZero(message = "El precio no puede ser un numero menor a 0")
    private BigDecimal precio;

    @PositiveOrZero(message = "El Stock no puede ser menor a 0")
    private Integer stock;

    @Size(max = 20, message = "La marca no puede exceder los 20 caracteres")
    private String marca;

    @PositiveOrZero(message = "El peso del alimento no puede ser un numero menor a 0")
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
