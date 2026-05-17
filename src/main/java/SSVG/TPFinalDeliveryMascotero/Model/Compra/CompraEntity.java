package SSVG.TPFinalDeliveryMascotero.Model.Compra;

import SSVG.TPFinalDeliveryMascotero.Model.CompraProducto.CompraProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.Proveedor.ProveedorEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Compras")
public class CompraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ProveedorEntity proveedor;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(nullable = false)
    private Double montoTotal;

    private List<CompraProductoEntity> productos;
}
