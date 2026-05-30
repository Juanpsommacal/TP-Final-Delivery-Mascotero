package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.AntipulgasCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AntipulgasResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.AntipulgasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/antipulgas")
public class AntipulgasController {

    private final AntipulgasService service;

    @PostMapping
    public ResponseEntity<AntipulgasResponseDTO> createAntipulgas(@Valid @RequestBody AntipulgasCreateRequestDTO request){
        AntipulgasResponseDTO response = service.createAntipulgas(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AntipulgasResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntipulgasResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AntipulgasResponseDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
