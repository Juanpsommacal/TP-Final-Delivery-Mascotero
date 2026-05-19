package SSVG.TPFinalDeliveryMascotero.Model.Producto;

import SSVG.TPFinalDeliveryMascotero.Model.DetalleCompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DetallePedidoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import SSVG.TPFinalDeliveryMascotero.Model.ProveedorEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Productos")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_producto")
public abstract class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String marca;

    @ManyToOne
    @JoinColumn(name = "oferta_id", nullable = true)
    private OfertaEntity oferta;

    @OneToMany(mappedBy = "producto")
    private List<DetalleCompraEntity> compras = new ArrayList<>();

    @OneToMany(mappedBy = "producto")
    private List<DetallePedidoEntity> pedidos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "producto_proveedor",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "proveedor_id")
    )
    private List<ProveedorEntity> proveedores = new ArrayList<>();
}
