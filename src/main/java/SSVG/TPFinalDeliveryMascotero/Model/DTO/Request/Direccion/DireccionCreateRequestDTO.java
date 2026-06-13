package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DireccionCreateRequestDTO {

    @NotBlank(message = "La calle no puede estar vacia")
    private String calle;

    @NotNull(message = "El numero no puede estar vacio")
    @PositiveOrZero(message = "El numero no puede ser negativo")
    private Integer numero;

    @PositiveOrZero(message = "El numero no puede ser negativo")
    private Integer piso;

    private String departamento;

    private String observaciones;

}
