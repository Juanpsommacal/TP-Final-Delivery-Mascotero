package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.ProveedorCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ProveedorResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService service;

    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> createProveedor(@Valid @RequestBody ProveedorCreateRequestDTO request){
        ProveedorResponseDTO response = service.createProveedor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }
}
