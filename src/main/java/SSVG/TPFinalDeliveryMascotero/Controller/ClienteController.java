package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ClienteResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        ClienteResponseDTO response = service.createCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @PatchMapping("/{clienteId}/direcciones/{direccionId}")
    public ResponseEntity<ClienteResponseDTO> associateDireccion(@PathVariable Long clienteId,
                                                               @PathVariable Long direccionId) {
        return ResponseEntity.ok(service.associateDireccion(clienteId, direccionId));
    }

    @DeleteMapping("/{clienteId}/direcciones/{direccionId}")
    public ResponseEntity<ClienteResponseDTO> dissociateDireccion(@PathVariable Long clienteId,
                                                                   @PathVariable Long direccionId) {
        return ResponseEntity.ok(service.dissociateDireccion(clienteId, direccionId));
    }



}
