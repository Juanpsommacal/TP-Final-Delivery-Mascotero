package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Usuario;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo puede contener letras")
    @Size(max = 30, message = "El apellido no puede exceder los 30 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo puede contener letras")
    @Size(max = 30, message = "El apellido no puede exceder los 30 caracteres")
    private String apellido;

    @NotNull
    @Email
    @Size(max = 40, message = "El mail no puede exceder los 40 caracteres")
    private String email;

    @NotNull
    @Size(min = 3, max = 8,message = "La contraseña debe contener entre 3 y 8 caracteres")
    private String  password;
}
