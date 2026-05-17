package SSVG.TPFinalDeliveryMascotero.Model.Direccion;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Direccion {

    private Long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private Integer numero;

    private Integer piso;

    private String departamento;

    private String observaciones;

}
