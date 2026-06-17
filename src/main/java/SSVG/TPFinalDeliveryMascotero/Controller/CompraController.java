package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra.CompraCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.CompraResponseDTO;
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
}
