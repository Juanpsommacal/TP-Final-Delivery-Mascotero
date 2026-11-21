package SSVG.TPFinalDeliveryMascotero.Model;

import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Ofertas")
public class OfertaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String nombre;

    @Column(length = 100)
    private String descripcion;

    @Column(nullable = false)
    private Double porcentaje;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @OneToMany(mappedBy = "oferta")
    private List<ProductoEntity> productos;

    // Ya termino: el producto queda libre para otra oferta
    public boolean estaVencida(LocalDate hoy) {
        return fechaFin != null && hoy.isAfter(fechaFin);
    }

    // Todavia no empezo
    public boolean estaProgramada(LocalDate hoy) {
        return fechaInicio != null && hoy.isBefore(fechaInicio);
    }

    public boolean estaVigente(LocalDate hoy) {
        return !estaVencida(hoy) && !estaProgramada(hoy);
    }

}
