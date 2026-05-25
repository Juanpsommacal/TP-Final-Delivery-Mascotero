package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request;


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

    @NotBlank
    private String nombre;

    private String descripcion;

    @NotNull
    @PositiveOrZero
    private BigDecimal precio;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    @NotBlank
    private String marca;

    @NotNull
    @PositiveOrZero
    private Double peso;

    @NotNull
    private UnidadMedida unidadMedida;

    @NotNull
    private EtapaVida etapaVida;

    @NotNull
    private TipoAnimal tipoAnimal;
}
