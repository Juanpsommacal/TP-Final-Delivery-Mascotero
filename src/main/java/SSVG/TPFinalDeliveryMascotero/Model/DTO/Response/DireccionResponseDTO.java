package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DireccionResponseDTO {

    private Long id;

    private String direccionCompleta;

    private String pisoDepto;

    private String observaciones;

    // Campos sueltos para que el front pueda comparar si una direccion que se esta cargando ya existe
    private String calle;

    private Integer numero;

    private Integer piso;

    private String departamento;

    // "Nombre Apellido" de cada cliente que usa esta direccion
    private List<String> clientes;

}
