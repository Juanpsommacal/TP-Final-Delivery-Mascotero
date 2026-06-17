package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.DetallePedido;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetallePedidoCreateRequestDTO {

    @Positive(message = "El ID del producto debe ser mayor a 0")
    @NotNull(message = "La id del producto no puede estar vacia")
    private Long productoId;

    @NotNull(message = "La cantidad no puede estar vacia")
    @Positive(message = "La cantidad debe ser un valor positivo")
    @Digits(integer = 2, fraction = 0, message = "La cantidad no puede tener mas de 2 digitos")
    private Integer cantidad;

}
