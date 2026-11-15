package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pago.PagoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Paginacion.PagosPaginaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PagoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.MetodoPago;
import SSVG.TPFinalDeliveryMascotero.Service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PagoResponseDTO> createPago(@Valid @RequestBody PagoCreateRequestDTO request){
        PagoResponseDTO response = service.createPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<PagoResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/pagina")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PagosPaginaResponseDTO> getPagina(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String orden,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) List<MetodoPago> metodos,
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta){
        return ResponseEntity.ok(service.getPagina(page, size, orden, q, metodos, desde, hasta));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PagoResponseDTO> getById(@PathVariable Long id) {
        PagoResponseDTO response = service.getDTOById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagoResponseDTO>> getByClienteId(@PathVariable Long clienteId){
        List<PagoResponseDTO> response = service.listPagosByClienteId(clienteId);
        return ResponseEntity.ok(response);
    }
}
