package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Service.ReportesService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ReportesService service;

    @GetMapping("/buscar-pedidos-por-direccion")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByDireccion(@RequestParam
                                                                             @NotBlank(message = "La calle no puede estar vacia") String calle,
                                                                         @RequestParam
                                                                         @Positive(message = "El numero debe ser positivo")
                                                                         @Digits(integer = 5, fraction = 0, message = "El numero no puede tener mas de 5 digitos")
                                                                                 @NotNull(message = "El numero no puede estar vacio")
                                                                         Integer numero){
        return ResponseEntity.ok(service.getPedidosByDireccion(calle, numero));
    }

    @GetMapping("/buscar-pedidos-por-fecha")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByFecha(@RequestParam @NotNull(message = "La fecha no puede estar vacia")  LocalDate fecha){
        return ResponseEntity.ok(service.getPedidosByFecha(fecha));
    }

    @GetMapping("/buscar-pedidos-por-estado")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByEstado(@RequestParam @NotNull(message = "El estado no puede estar vacio") EstadoPedido estado){
        return ResponseEntity.ok(service.getPedidosByEstado(estado));
    }

}
