package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
public class AlimentoResponseDTO {

    private String marca;

    private String nombre;

    private Double peso;

    private UnidadMedida unidadMedida;

    private String descripcion;

    private BigDecimal precio;

    private Integer stock;

    private EtapaVida etapaVida;

    private TipoAnimal tipoAnimal;
}
