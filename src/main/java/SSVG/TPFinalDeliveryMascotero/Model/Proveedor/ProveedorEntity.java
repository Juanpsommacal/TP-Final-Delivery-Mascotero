package SSVG.TPFinalDeliveryMascotero.Model.Proveedor;

import jakarta.persistence.*;

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

}
