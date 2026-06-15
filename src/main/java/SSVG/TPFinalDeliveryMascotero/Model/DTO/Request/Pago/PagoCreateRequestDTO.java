package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pago;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.MetodoPago;
import jakarta.validation.constraints.*;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
import SSVG.TPFinalDeliveryMascotero.Validations.ValidEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagoCreateRequestDTO {

    @NotNull(message = "El ID del Pedido no puede estar vacio, es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El monto no puede estar vacio")
    @Positive(message = "El monto debe ser un valor positivo")
    @Digits(integer = 7, fraction = 2, message = "El pago no puede tener mas de 7 digitos")
    private BigDecimal monto;

    @NotNull(message = "El metodo de pago no puede estar vacio")
    @ValidEnum(enumClass = MetodoPago.class, message = "El metodo de pago debe ser EFECTIVO, LINK_DE_PAGO, TRASNFERENCIA")
    @Size(max = 30, message = "El metodo de pago no puede exceder los 30 caracteres")
    private String metodoPago;

}
