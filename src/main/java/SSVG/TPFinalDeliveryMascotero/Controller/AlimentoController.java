package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.AlimentoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AlimentoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.AlimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final AlimentoService service;

    @PostMapping
    public ResponseEntity<AlimentoResponseDTO> createAlimento(@Valid @RequestBody AlimentoCreateRequestDTO request){

        AlimentoResponseDTO response = service.createAlimento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AlimentoResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlimentoResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AlimentoResponseDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
