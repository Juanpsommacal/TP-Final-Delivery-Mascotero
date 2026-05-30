package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.AntipulgasMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.AntipulgasCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AntipulgasResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.AntipulgasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
//Service para operaciones especificas de Antipulgas (clase hija). (crearAntipulgas, EditarAntipulgas)
public class AntipulgasService {

    private final AntipulgasRepository repository;
    private final AntipulgasMapper mapper;

    public AntipulgasResponseDTO createAntipulgas(AntipulgasCreateRequestDTO request){
        AntipulgasEntity newAntipulgas = mapper.toEntity(request);
        return mapper.toResponse(repository.save(newAntipulgas));
    }

    public AntipulgasEntity getEntityById(Long id){
        Optional<AntipulgasEntity> entity = repository.findById(id);
        if(entity.isPresent())
            return entity.get();
        else throw new ResourceNotFoundException("El antipulgas no existe");
    }

    public AntipulgasResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<AntipulgasResponseDTO> getAll(){
        return repository.findByActivoTrue().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void deleteById(Long id){
        AntipulgasEntity entity = getEntityById(id);
        if(entity.getActivo() == false)
            throw new InactiveResourceException("El producto ya fue eliminado");

        entity.setActivo(false);
        repository.save(entity);
    }

    ///----- Validations -----
}
