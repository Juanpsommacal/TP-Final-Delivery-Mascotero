package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PatchMapping("/{productoId}/oferta/{ofertaId}")
    public ResponseEntity<ProductoResponseDTO> asignarOferta(
            @PathVariable Long productoId,
            @PathVariable Long ofertaId) {

        return ResponseEntity.ok(
                service.asignarOferta(productoId, ofertaId)
        );
    }
}