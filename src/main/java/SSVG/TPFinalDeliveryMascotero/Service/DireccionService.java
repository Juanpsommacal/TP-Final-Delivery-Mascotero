package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyUpdateFieldException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.DireccionMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.DireccionResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.DireccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DireccionService {

    private final DireccionRepository repository;
    private final DireccionMapper mapper;

    public DireccionResponseDTO createDireccion(DireccionCreateRequestDTO request){
        DireccionEntity entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    public DireccionEntity getEntityById(Long id){
        Optional<DireccionEntity> entity = repository.findById(id);
        if(entity.isPresent())
            return entity.get();
        else throw new ResourceNotFoundException("La direccion no existe");
    }

    public DireccionResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<DireccionResponseDTO> getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    ///----- Updates -----

    public DireccionResponseDTO updateDireccion(DireccionUpdateRequestDTO request, Long id){
        DireccionEntity entity = getEntityById(id);

        if(request.getCalle() != null){
            if(request.getCalle().trim().isEmpty())
                throw new EmptyUpdateFieldException("La calle no puede ser solo espacios en blanco");
            entity.setCalle(request.getCalle());
        }
        if(request.getNumero() != null){
            entity.setNumero(request.getNumero());
        }
        if(request.getPiso() != null){
            entity.setPiso(request.getPiso());
        }
        if(request.getDepartamento() != null){
            if(request.getDepartamento().trim().isEmpty())
                throw new EmptyUpdateFieldException("El departamento no puede ser solo espacios en blanco");
            entity.setDepartamento(request.getDepartamento());
        }
        if (request.getObservaciones() != null) {
            if(request.getObservaciones().trim().isEmpty())
                throw new EmptyUpdateFieldException("Las observaciones no pueden ser solo espacios en blanco");
            entity.setObservaciones(request.getObservaciones());
        }

        return mapper.toResponse(repository.save(entity));

    }


    ///----- Validations -----

}
