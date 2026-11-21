package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProductoResponseDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private BigDecimal precioOferta;

    private Integer stock;

    private String marca;

    private Boolean activo;

    private String tipoProducto;

    // Lo que distingue a productos del mismo nombre: "3 kg" o "De 10 a 20 kg"
    private String presentacion;

    // Oferta asociada (todo null si no tiene). ofertaEstado: VIGENTE, PROGRAMADA o VENCIDA
    private Long ofertaId;
    private String ofertaNombre;
    private Double ofertaPorcentaje;
    private LocalDate ofertaFechaInicio;
    private LocalDate ofertaFechaFin;
    private String ofertaEstado;

}
