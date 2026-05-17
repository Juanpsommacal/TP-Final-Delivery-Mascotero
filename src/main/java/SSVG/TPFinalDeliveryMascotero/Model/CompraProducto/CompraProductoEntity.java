package SSVG.TPFinalDeliveryMascotero.Model.CompraProducto;

import SSVG.TPFinalDeliveryMascotero.Model.Compra.CompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Compra_Producto")
public class CompraProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CompraEntity compra;

    private ProductoEntity producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

}
