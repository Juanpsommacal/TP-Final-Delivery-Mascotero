package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ClienteResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteCreateRequestDTO request){
        ClienteResponseDTO response = service.createCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{clienteId}/direcciones")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClienteResponseDTO> associateDireccion(@PathVariable Long clienteId,
                                                                 @Valid @RequestBody DireccionCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.associateDireccion(clienteId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ClienteResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClienteResponseDTO> updateCliente(@Valid @RequestBody ClienteUpdateRequestDTO request, @PathVariable Long id){
        return ResponseEntity.ok(service.updateCliente(request, id));
    }

    @DeleteMapping("/{clienteId}/direcciones/{direccionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClienteResponseDTO> dissociateDireccion(@PathVariable Long clienteId,
                                                                   @PathVariable Long direccionId) {
        return ResponseEntity.ok(service.dissociateDireccion(clienteId, direccionId));
    }



}
