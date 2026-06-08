package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class OfertaResponseDTO {

    private String nombre;

    private String descripcion;

    private Double porcentaje;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

}
