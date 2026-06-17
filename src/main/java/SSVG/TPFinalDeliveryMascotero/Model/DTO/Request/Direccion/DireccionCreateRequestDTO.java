package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion;

import jakarta.validation.constraints.*;
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
    @Size(max = 30, message = "El nombre de la calle no puede exceder los 30 caracteres")
    private String calle;

    @NotNull(message = "El numero no puede estar vacio")
    @PositiveOrZero(message = "El numero no puede ser negativo")
    @Digits(integer = 5, fraction = 0, message = "El numero de la calle no puede tener mas de 5 digitos")
    private Integer numero;

    @PositiveOrZero(message = "El numero no puede ser negativo")
    @Digits(integer = 2, fraction = 0, message = "El numero de piso no puede tener mas de 2 digitos")
    private Integer piso;

    @Size(max = 10, message = "El departamento no puede exceder los 10 caracteres")
    private String departamento;

    @Size(max = 50, message = "Las observaciones no pueden exceder los 50 caracteres")
    private String observaciones;
}
