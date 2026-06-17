package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Oferta.OfertaCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.OfertaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
@RequiredArgsConstructor
public class OfertaController {

    private final OfertaService service;

    //QUITA DE LA OFERTA UN SOLO PRODUCTO
    @PatchMapping("/{ofertaId}/producto/{productoId}/remove")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OfertaResponseDTO> removeProductFromOffer(
            @PathVariable Long ofertaId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(service.removeProductFromOffer(ofertaId, productoId)
        );
    }


    // CREA OFERTA..
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OfertaResponseDTO> createOferta(@Valid @RequestBody OfertaCreateRequestDTO requestDTO) {
        OfertaResponseDTO response = service.createOferta(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<OfertaResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OfertaResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDTOById(id));
    }

    // GET OFERTAS ACTIVAS
    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<OfertaResponseDTO>> getAllActive() {
        return ResponseEntity.ok(service.getAllActive());
    }

    // UPDATE OFERTA (NO AGREGA PRODUCTOS QUE YA POSEEN OFERTA)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfertaResponseDTO> updateOferta(@PathVariable Long id,
                                                          @Valid @RequestBody OfertaCreateRequestDTO request) {
        return ResponseEntity.ok(service.updateOferta(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOferta(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ofertaId}/producto/{productoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OfertaResponseDTO> associateProduct(
            @PathVariable Long ofertaId,
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                service.associatedProductToOffer(ofertaId, productoId)
        );
    }

}