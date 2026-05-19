package SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;

@Entity
public class Antipulgas {

    @Id
    private Long productoId;

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

    @OneToOne
    @MapsId
    @JoinColumn(name = "producto_id")
    private ProductoEntity producto;
}
