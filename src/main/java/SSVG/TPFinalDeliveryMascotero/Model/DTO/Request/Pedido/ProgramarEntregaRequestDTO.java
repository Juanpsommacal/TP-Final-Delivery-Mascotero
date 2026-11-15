package SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProgramarEntregaRequestDTO {

    @NotNull(message = "La fecha de entrega es obligatoria")
    @FutureOrPresent(message = "La fecha de entrega no puede ser anterior a hoy")
    private LocalDate fechaEntrega;
}
