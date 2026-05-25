package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.AlimentoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AlimentoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.AlimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
