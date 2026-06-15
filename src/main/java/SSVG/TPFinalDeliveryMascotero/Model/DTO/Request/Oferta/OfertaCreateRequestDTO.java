package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Oferta;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OfertaCreateRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El porcentaje es obligatorio")
    @DecimalMin(value = "0.01", message = "El porcentaje debe ser mayor a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje cargado no puede ser mayor a 100")
    private Double porcentaje;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser una fecha anterior a la actual")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @FutureOrPresent(message = "La fecha de fin no puede ser una fecha anterior a la de inicio")
    private LocalDate fechaFin;

    // Lista de IDs de productos asociados
    private List<Long> productosIds;
}
