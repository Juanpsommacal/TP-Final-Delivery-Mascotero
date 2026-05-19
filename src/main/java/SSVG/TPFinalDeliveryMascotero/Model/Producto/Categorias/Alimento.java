package SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;

@Entity
public class Alimento {

    @Id
    private Long productoId;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UnidadMedida unidadMedida;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EtapaVida etapaVida;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAnimal tipoAnimal;

    @OneToOne
    @MapsId
    @JoinColumn(name = "producto_id")
    private ProductoEntity producto;
}
