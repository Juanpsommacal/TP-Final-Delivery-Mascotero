package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.DetalleCompra;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetalleCompraCreateRequestDTO {

    @NotNull(message = "La id del producto no puede estar vacia")
    private Long productoId;

    @NotNull(message = "La cantidad no puede estar vacia")
    @Positive(message = "La cantidad debe ser un valor positivo")
    private Integer cantidad;

    @NotNull(message = "El precio unitario no puede estar vacio")
    @PositiveOrZero(message = "El precio unitario debe ser mayor o igual a 0")
    private BigDecimal precioUnitario;


}
