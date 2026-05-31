package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteResponseDTO {

    private String nombre;

    private String apellido;

    private String telefono;

    private List<DireccionResponseDTO> direcciones = new ArrayList<>();

}
