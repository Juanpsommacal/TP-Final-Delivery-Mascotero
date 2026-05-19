package SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "alimentos")
@PrimaryKeyJoinColumn(name = "producto_id")
@DiscriminatorValue("ALIMENTO")
public class AlimentoEntity extends ProductoEntity {

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

}
