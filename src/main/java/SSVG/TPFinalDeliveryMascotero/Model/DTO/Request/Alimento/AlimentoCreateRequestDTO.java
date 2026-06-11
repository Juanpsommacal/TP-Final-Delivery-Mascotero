package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento;


import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
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
public class AlimentoCreateRequestDTO {

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

    @NotNull(message = "El peso no puede estar vacio")
    @PositiveOrZero(message = "El peso no puede ser menor a 0")
    private Double peso;

    @NotNull(message = "La unidad de medida no puede estar vacia")
    private UnidadMedida unidadMedida;

    @NotNull(message = "La etapa de vida no puede estar vacia")
    private EtapaVida etapaVida;

    @NotNull(message = "El tipo de animal no puede estar vacio")
    private TipoAnimal tipoAnimal;
}
