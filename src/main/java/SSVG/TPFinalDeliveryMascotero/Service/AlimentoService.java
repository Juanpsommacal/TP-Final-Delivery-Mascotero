package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Mapper.AlimentoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.AlimentoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AlimentoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//Service para operaciones especificas de Alimento (clase hija). (crearAlimento, EditarAlimento)
public class AlimentoService {

    private final AlimentoMapper mapper;
    private final AlimentoRepository repository;

    public AlimentoResponseDTO createAlimento(AlimentoCreateRequestDTO request){

        AlimentoEntity newAlimento = mapper.toEntity(request);
        return mapper.toResponse(repository.save(newAlimento));

    }

    ///----- Validations -----

}
