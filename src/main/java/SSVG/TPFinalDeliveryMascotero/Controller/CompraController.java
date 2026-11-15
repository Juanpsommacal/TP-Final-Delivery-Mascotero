package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra.CompraCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.CompraResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion.ComprasPaginaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.UltimoPrecioCompraResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoCompra;
import SSVG.TPFinalDeliveryMascotero.Service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CompraResponseDTO> createCompra(@Valid @RequestBody CompraCreateRequestDTO request){
        CompraResponseDTO response = service.createCompra(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CompraResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/ultimos-precios")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UltimoPrecioCompraResponseDTO>> getUltimosPrecios(){
        return ResponseEntity.ok(service.getUltimosPrecios());
    }

    @GetMapping("/proveedor/{proveedorId}/productos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Long>> getProductoIdsDeProveedor(@PathVariable Long proveedorId){
        return ResponseEntity.ok(service.getProductoIdsDeProveedor(proveedorId));
    }

    @GetMapping("/pagina")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ComprasPaginaResponseDTO> getPagina(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String orden,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) EstadoCompra estado,
            @RequestParam(defaultValue = "false") boolean canceladas){
        return ResponseEntity.ok(service.getPagina(page, size, orden, q, estado, canceladas));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CompraResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @PatchMapping("/{id}/recibir")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CompraResponseDTO> receiveCompra(@PathVariable Long id){
        return ResponseEntity.ok(service.receiveCompra(id));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CompraResponseDTO> cancelCompra(@PathVariable Long id){
        return ResponseEntity.ok(service.cancelCompra(id));
    }
}
