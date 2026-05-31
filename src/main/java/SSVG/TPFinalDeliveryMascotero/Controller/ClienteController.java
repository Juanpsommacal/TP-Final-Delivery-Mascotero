package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ClienteResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteCreateRequestDTO request){
        return ResponseEntity.ok(service.createCliente(request));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(@Valid @RequestBody ClienteUpdateRequestDTO request, @PathVariable Long id){
        return ResponseEntity.ok(service.updateCliente(request, id));
    }

}
