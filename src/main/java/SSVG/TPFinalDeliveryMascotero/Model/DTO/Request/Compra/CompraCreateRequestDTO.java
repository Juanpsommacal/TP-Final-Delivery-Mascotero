package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.DetalleCompra.DetalleCompraCreateRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompraCreateRequestDTO {

    @Positive(message = "El ID del proveedor debe ser mayor a 0")
    @NotNull(message = "El campo proveedor no puede estar vacio")
    private Long proveedorId;

    @NotEmpty(message = "El detalle de la compra no puede estar vacio")
    @Valid
    private List<DetalleCompraCreateRequestDTO> detalle;

}
