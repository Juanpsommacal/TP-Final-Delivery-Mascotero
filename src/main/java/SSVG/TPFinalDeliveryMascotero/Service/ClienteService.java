package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyUpdateFieldException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.ClienteMapper;
import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ClienteResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public ClienteResponseDTO createCliente(ClienteCreateRequestDTO request){
        ClienteEntity newCliente = mapper.toEntity(request);
        return mapper.toResponse(repository.save(newCliente));
    }

    public ClienteEntity getEntityById(Long id){
        Optional<ClienteEntity> entity = repository.findById(id);
        if (entity.isPresent())
            return entity.get();
        else throw new ResourceNotFoundException("El cliente no existe");
    }

    public ClienteResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<ClienteResponseDTO> getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    ///----- Updates -----

    public ClienteResponseDTO updateCliente(ClienteUpdateRequestDTO request, Long id){
        ClienteEntity entity = getEntityById(id);

        if (request.getNombre() != null){
            if (request.getNombre().trim().isEmpty())
                throw new EmptyUpdateFieldException("El nombre no puede ser solo espacios en blanco");
            entity.setNombre(request.getNombre());
        }
        if (request.getApellido() != null){
            if (request.getApellido().trim().isEmpty())
                throw new EmptyUpdateFieldException("El apellido no puede ser solo espacios en blanco");
            entity.setApellido(request.getApellido());
        }
        if (request.getTelefono() != null) {
            if (request.getTelefono().trim().isEmpty())
                throw new EmptyUpdateFieldException("El telefono no puede ser solo espacios en blanco");
            entity.setTelefono(request.getTelefono());
        }
        return mapper.toResponse(repository.save(entity));
    }


    ///----- Validations -----


}
