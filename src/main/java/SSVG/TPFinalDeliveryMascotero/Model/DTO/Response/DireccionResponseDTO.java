package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DireccionResponseDTO {

    private Long id;

    private String direccionCompleta;

    private String pisoDepto;

    private String observaciones;

}
