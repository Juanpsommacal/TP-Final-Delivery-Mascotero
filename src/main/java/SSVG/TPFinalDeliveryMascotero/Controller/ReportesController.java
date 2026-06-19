package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Reportes.*;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Service.ReportesService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ReportesService service;

    /// ----- Busqueda de pedidos
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/buscar-pedidos-por-direccion")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByDireccion(@RequestParam
                                                                             @NotBlank(message = "La calle no puede estar vacia") String calle,
                                                                         @RequestParam
                                                                         @Positive(message = "El numero debe ser positivo")
                                                                         @Digits(integer = 5, fraction = 0, message = "El numero no puede tener mas de 5 digitos")
                                                                                 @NotNull(message = "El numero no puede estar vacio")
                                                                         Integer numero){
        return ResponseEntity.ok(service.getPedidosByDireccion(calle, numero));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/buscar-pedidos-por-fecha")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByFecha(@RequestParam @NotNull(message = "La fecha no puede estar vacia")  LocalDate fecha){
        return ResponseEntity.ok(service.getPedidosByFecha(fecha));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/buscar-pedidos-por-estado")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByEstado(@RequestParam @NotNull(message = "El estado no puede estar vacio") EstadoPedido estado){
        return ResponseEntity.ok(service.getPedidosByEstado(estado));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/buscar-pedidos-por-estado-pago")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByEstadoPago(@RequestParam @NotNull(message = "El estado no puede estar vacio")EstadoPago estado){
        return ResponseEntity.ok(service.getPedidosByEstadoPago(estado));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscar-cantidad-pedidos-por-estado-por-mes")
    public ResponseEntity<List<CantidadPedidosPorEstadoResponseDTO>> getCantidadPedidosPorEstado(@RequestParam @NotNull(message = "El año es obligatorio") Integer anio,
                                                                                                 @RequestParam @Min(1) @Max(12) @NotNull(message = "El mes debe ser entre 1 y 12") Integer mes){
        return ResponseEntity.ok(service.getCantidadPedidosPorEstadoPorMes(anio, mes));
    }

    /// ----- Busqueda de usuarios

    /// ----- Metricas

    //Solo suma los pedidos que esten entregados y pagados
    @GetMapping("/calcular-total-ventas-por-mes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VentasPorMesResponseDTO> getTotalVentasByMes(@RequestParam @NotNull(message = "El año es obligatorio") Integer anio,
                                                                         @RequestParam @Min(1) @Max(12) @NotNull(message = "El mes debe ser entre 1 y 12") Integer mes){
        return ResponseEntity.ok(service.getVentasByMes(anio, mes));
    }

    @GetMapping("/calcular-ventas-por-rango")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VentasPorRangoResponseDTO> getVentasByRango(@RequestParam @NotNull(message = "La fecha de inicio es obligatoria") LocalDate fechaInicio,
                                                                       @RequestParam @NotNull(message = "La fecha de fin es obligatoria") LocalDate fechaFin){
        return ResponseEntity.ok(service.getVentasByRango(fechaInicio, fechaFin));
    }

    @GetMapping("/calcular-ticket-promedio-mes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketPromedioResponseDTO> getTicketPromedioVentas(@RequestParam @NotNull(message = "El año es obligatorio") Integer anio,
                                                                             @RequestParam @Min(1) @Max(12) @NotNull(message = "El mes debe ser entre 1 y 12") Integer mes){
        return ResponseEntity.ok(service.getTicketPromedioVentas(anio, mes));
    }

    @GetMapping("/buscar-top-5-mas-vendidos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ProductoMasVendidoResponseDTO>> getTop5ProductosMasVendidos(){
        return ResponseEntity.ok(service.getTop5ProductosMasVendidos());
    }

}
