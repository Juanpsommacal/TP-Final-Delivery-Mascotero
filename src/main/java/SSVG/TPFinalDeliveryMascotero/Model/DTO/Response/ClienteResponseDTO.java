package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClienteResponseDTO {

    private String nombre;

    private String apellido;

    private String telefono;

    private List<DireccionResponseDTO> direcciones = new ArrayList<>();

}
