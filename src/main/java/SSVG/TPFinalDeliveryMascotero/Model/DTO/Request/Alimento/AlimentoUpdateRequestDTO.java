package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
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
public class AlimentoUpdateRequestDTO {

    private String nombre;

    private String descripcion;

    @PositiveOrZero(message = "El precio no puede ser un numero menor a 0")
    private BigDecimal precio;

    @PositiveOrZero(message = "El Stock no puede ser menor a 0")
    private Integer stock;

    private String marca;

    @PositiveOrZero(message = "El peso del alimento no puede ser un numero menor a 0")
    private Double peso;

    private UnidadMedida unidadMedida;

    private EtapaVida etapaVida;

    private TipoAnimal tipoAnimal;
}
