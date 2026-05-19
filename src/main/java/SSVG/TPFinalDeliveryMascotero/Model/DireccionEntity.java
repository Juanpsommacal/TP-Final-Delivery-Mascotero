package SSVG.TPFinalDeliveryMascotero.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
