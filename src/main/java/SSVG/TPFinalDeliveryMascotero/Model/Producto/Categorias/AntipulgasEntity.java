package SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "antipulgas")
@PrimaryKeyJoinColumn(name = "producto_id")
@DiscriminatorValue("ANTIPULGAS")
public class AntipulgasEntity extends ProductoEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAnimal tipoAnimal;

    @Column(nullable = false)
    private Double kgMin;

    @Column(nullable = false)
    private Double kgMax;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAntipulgas tipoAntipulgas;

}
