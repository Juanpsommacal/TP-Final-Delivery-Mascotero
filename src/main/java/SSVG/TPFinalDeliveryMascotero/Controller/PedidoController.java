package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.OrdenRepartoRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.ProgramarEntregaRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.FaltanteProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion.PedidosPaginaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponseDTO> createPedido(@Valid @RequestBody PedidoCreateRequestDTO request){
        PedidoResponseDTO response = service.createPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PedidoResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/contadores")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Map<EstadoPedido, Long>> getContadores(){
        return ResponseEntity.ok(service.getContadores());
    }

    @GetMapping("/faltantes")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<FaltanteProductoResponseDTO>> getFaltantes(){
        return ResponseEntity.ok(service.getFaltantes());
    }

    @GetMapping("/reparto")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PedidoResponseDTO>> getReparto(@RequestParam(required = false) LocalDate fecha){
        return ResponseEntity.ok(service.getReparto(fecha));
    }

    @PutMapping("/reparto/orden")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> guardarOrdenReparto(@Valid @RequestBody OrdenRepartoRequestDTO request){
        service.guardarOrdenReparto(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PedidoResponseDTO>> getByClienteId(@PathVariable Long clienteId){
        return ResponseEntity.ok(service.getByClienteId(clienteId));
    }

    @GetMapping("/pagina")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidosPaginaResponseDTO> getPagina(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String orden,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) EstadoPedido estado,
            @RequestParam(required = false) EstadoPago estadoPago,
            @RequestParam(defaultValue = "false") boolean conDeuda,
            @RequestParam(defaultValue = "false") boolean cancelados,
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta){
        return ResponseEntity.ok(service.getPagina(page, size, orden, q, estado, estadoPago, conDeuda, cancelados, desde, hasta));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponseDTO> deletePedido(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/mercaderia")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponseDTO> confirmarMercaderia(@PathVariable Long id){
        return ResponseEntity.ok(service.confirmarMercaderia(id));
    }

    @PatchMapping("/{id}/programar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponseDTO> programarEntrega(@PathVariable Long id, @Valid @RequestBody ProgramarEntregaRequestDTO request){
        return ResponseEntity.ok(service.programarEntrega(id, request));
    }

    @PatchMapping("/{id}/a-coordinar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponseDTO> volverACoordinar(@PathVariable Long id){
        return ResponseEntity.ok(service.volverACoordinar(id));
    }

    @PatchMapping("/{id}/recordatorio")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponseDTO> registrarRecordatorio(@PathVariable Long id){
        return ResponseEntity.ok(service.registrarRecordatorio(id));
    }

    @PatchMapping("/{id}/entregar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponseDTO> deliverPedido(@PathVariable Long id){
        return ResponseEntity.ok(service.deliverPedido(id));
    }
}
