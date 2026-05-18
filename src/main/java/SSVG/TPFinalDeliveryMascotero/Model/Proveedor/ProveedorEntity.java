package SSVG.TPFinalDeliveryMascotero.Model.Proveedor;

import SSVG.TPFinalDeliveryMascotero.Model.Compra.CompraEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Proveedores")
public class ProveedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "proveedor")
    private List<CompraEntity> compras = new ArrayList<>();

    @ManyToMany(mappedBy = "proveedores")
    private List<ProductoEntity> productos = new ArrayList<>();

}
