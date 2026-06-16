package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> createPedido(@Valid @RequestBody PedidoCreateRequestDTO request){
        PedidoResponseDTO response = service.createPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDTOById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> deletePedido(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<PedidoResponseDTO> deliverPedido(@PathVariable Long id){
        return ResponseEntity.ok(service.deliverPedido(id));
    }
}
