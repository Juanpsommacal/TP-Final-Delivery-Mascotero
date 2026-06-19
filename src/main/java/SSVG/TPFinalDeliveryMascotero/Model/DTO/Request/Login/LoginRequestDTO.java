package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank
    @Email
    @Size(max = 40, message = "El mail no puede exceder los 40 caracteres")
    private String email;
    @NotBlank
    @Size(min = 3, max = 8,message = "La contraseña debe contener entre 3 y 8 caracteres")
    private String password;
}
