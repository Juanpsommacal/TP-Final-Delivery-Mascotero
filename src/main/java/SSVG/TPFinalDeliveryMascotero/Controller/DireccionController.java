package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.DireccionResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/direcciones")
public class DireccionController {

    private final DireccionService service;

    @PostMapping
    public ResponseEntity<DireccionResponseDTO> createDireccion(@Valid @RequestBody DireccionCreateRequestDTO request){
        DireccionResponseDTO response = service.createDireccion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DireccionResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> updateDireccion(@Valid @RequestBody DireccionUpdateRequestDTO request, @PathVariable Long id){
        return ResponseEntity.ok(service.updateDireccion(request,id));
    }

}
