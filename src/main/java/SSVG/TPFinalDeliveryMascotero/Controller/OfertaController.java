package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra.OfertaRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
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

    private final OfertaService ofertaService;

    @PostMapping
    public ResponseEntity<OfertaResponseDTO> create(
            @Valid @RequestBody OfertaRequestDTO requestDTO) {

        OfertaResponseDTO response = ofertaService.create(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping
    public ResponseEntity<List<OfertaResponseDTO>> getAll() {

        return ResponseEntity.ok(
                ofertaService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ofertaService.getById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> update(
            @PathVariable Long id,
            @RequestBody OfertaRequestDTO requestDTO) {

        return ResponseEntity.ok(
                ofertaService.update(id, requestDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        ofertaService.delete(id);

        return ResponseEntity.noContent().build();
    }
}