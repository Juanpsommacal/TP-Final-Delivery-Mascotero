package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }


}