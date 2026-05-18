package SSVG.TPFinalDeliveryMascotero.Model.DetalleCompra;

import SSVG.TPFinalDeliveryMascotero.Model.Compra.CompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "Detalle_Compra")
public class DetalleCompraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @MapsId("compraId")
    @JoinColumn(name = "compra_id")
    private CompraEntity compra;

    @ManyToOne
    @MapsId("productoId")
    @JoinColumn(name = "producto_id")
    private ProductoEntity producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

}
