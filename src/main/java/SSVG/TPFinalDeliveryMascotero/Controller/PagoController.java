package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pago.PagoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PagoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER)")
    public ResponseEntity<PagoResponseDTO> createPago(@Valid @RequestBody PagoCreateRequestDTO request){
        PagoResponseDTO response = service.createPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER)")
    public ResponseEntity<PagoResponseDTO> getById(@PathVariable Long id) {
        PagoResponseDTO response = service.getDTOById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER)")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagoResponseDTO>> getByClienteId(@PathVariable Long clienteId){
        List<PagoResponseDTO> response = service.listPagosByClienteId(clienteId);
        return ResponseEntity.ok(response);
    }
}
