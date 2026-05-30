package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.AlimentoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.AlimentoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AlimentoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
//Service para operaciones especificas de Alimento (clase hija). (crearAlimento, EditarAlimento)
public class AlimentoService {

    private final AlimentoMapper mapper;
    private final AlimentoRepository repository;

    public AlimentoResponseDTO createAlimento(AlimentoCreateRequestDTO request){
        AlimentoEntity newAlimento = mapper.toEntity(request);
        return mapper.toResponse(repository.save(newAlimento));

    }

    public AlimentoEntity getEntityById(Long id){
        Optional<AlimentoEntity> entity = repository.findById(id);
        if(entity.isPresent())
            return entity.get();
        else throw new ResourceNotFoundException("El alimento no existe.");
    }

    public AlimentoResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<AlimentoResponseDTO> getAll(){
        return repository.findByActivoTrue().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void deleteById(Long id){
        AlimentoEntity entity = getEntityById(id);
        if(entity.getActivo() == false)
            throw new InactiveResourceException("El producto ya fue eliminado");

        entity.setActivo(false);
        repository.save(entity);
    }

    ///----- Validations -----

}
