package SSVG.TPFinalDeliveryMascotero.Model.Direccion;
import SSVG.TPFinalDeliveryMascotero.Model.Cliente.ClienteEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Direcciones")
public class DireccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private Integer numero;

    private Integer piso;

    private String departamento;

    private String observaciones;

    @ManyToMany(mappedBy = "direcciones")
    private List<ClienteEntity> clientes = new ArrayList<>();

}
