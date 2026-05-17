package SSVG.TPFinalDeliveryMascotero.Model.Producto;

import SSVG.TPFinalDeliveryMascotero.Model.Oferta.OfertaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "Productos")
public class ProductoEntity {

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

    private OfertaEntity oferta;
}
