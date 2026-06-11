package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AntipulgasResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.AntipulgasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/antipulgas")
public class AntipulgasController {

    private final AntipulgasService service;

    @PostMapping
    public ResponseEntity<AntipulgasResponseDTO> createAntipulgas(@Valid @RequestBody AntipulgasCreateRequestDTO request){
        AntipulgasResponseDTO response = service.createAntipulgas(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AntipulgasResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntipulgasResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AntipulgasResponseDTO> updateAntipulgas(@Valid @RequestBody AntipulgasUpdateRequestDTO request,
                                                                  @PathVariable Long id){
        return ResponseEntity.ok(service.updateAntipulgas(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AntipulgasResponseDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
