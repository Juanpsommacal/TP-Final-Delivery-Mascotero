package SSVG.TPFinalDeliveryMascotero.Model;

import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
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
