package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Oferta.OfertaCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.OfertaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
@RequiredArgsConstructor
public class OfertaController {

    private final OfertaService service;
    //quita productos de odertas

    @DeleteMapping("/{ofertaId}/delete-products")
    public ResponseEntity<OfertaResponseDTO> removeAllProductsFromOffer(
            @PathVariable Long ofertaId) {

        OfertaResponseDTO response =
                service.removeAllProductsFromOffer(ofertaId);

        return ResponseEntity.ok(response);
    }
    //quita oferta a Un solo Producto.
    @PatchMapping("/{ofertaId}/producto/{productoId}/remove")
    public ResponseEntity<OfertaResponseDTO> removeProductFromOffer(
            @PathVariable Long ofertaId,
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                service.removeProductFromOffer(ofertaId, productoId)
        );
    }

    @PatchMapping("/{ofertaId}/all-products")
    public ResponseEntity<OfertaResponseDTO> associateAllProducts(
            @PathVariable Long ofertaId) {

        return ResponseEntity.ok(
                service.associateAllProductsToOffer(ofertaId)
        );
    }

    @PostMapping
    public ResponseEntity<OfertaResponseDTO> create(@Valid @RequestBody OfertaCreateRequestDTO requestDTO) {
        OfertaResponseDTO response = service.createOferta(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OfertaResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> updateOferta(@PathVariable Long id,
                                                          @RequestBody OfertaCreateRequestDTO request)
    {
        return ResponseEntity.ok(service.updateOffer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ofertaId}/producto/{productoId}")
    public ResponseEntity<OfertaResponseDTO> associateProduct(
            @PathVariable Long ofertaId,
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                service.associateProductToOffer(ofertaId, productoId)
        );
    }

}