package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCreateRequestDTO {

    @NotBlank(message = "El nombre del cliente no puede estar vacio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El apellido del cliente no puede estar vacio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    @NotBlank(message = "El telefono no puede estar vacio")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El número de teléfono no es válido")
    private String telefono;

}
