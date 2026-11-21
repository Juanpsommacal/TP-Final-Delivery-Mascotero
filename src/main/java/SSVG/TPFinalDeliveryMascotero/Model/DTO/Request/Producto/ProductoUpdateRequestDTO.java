package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Producto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Campos que se pueden editar en cualquier tipo de producto.
// ProductoBaseService los lee sin saber si el producto es un alimento o un antipulgas.
@Getter
@Setter
@NoArgsConstructor
public abstract class ProductoUpdateRequestDTO {

    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    private String nombre;

    @Size(max = 100, message = "La descripcion no puede exceder los 100 caracteres")
    private String descripcion;

    @PositiveOrZero(message = "El precio no puede ser menor a 0")
    @Digits(integer = 7, fraction = 2, message = "El precio no puede tener mas de 7 digitos")
    private BigDecimal precio;

    @PositiveOrZero(message = "El stock no puede ser menor a 0")
    @Digits(integer = 3, fraction = 0, message = "El stock no puede tener mas de 3 digitos")
    private Integer stock;

    @Size(max = 20, message = "La marca no puede exceder los 20 caracteres")
    private String marca;
}
