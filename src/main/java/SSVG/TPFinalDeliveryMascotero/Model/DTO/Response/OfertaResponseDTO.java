package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OfertaResponseDTO {

    private String nombre;

    private String descripcion;

    private Double porcentaje;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

}
