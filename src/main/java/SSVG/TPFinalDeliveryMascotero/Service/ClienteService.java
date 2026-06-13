package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyUpdateFieldException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotAssociatedException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.ClienteMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.DireccionMapper;
import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ClienteResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final DireccionService direccionService;
    private final DireccionMapper direccionMapper;
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

    // Crea y Asocia una direccion a un Cliente
    @Transactional
    public ClienteResponseDTO associateDireccion(Long clienteId, DireccionCreateRequestDTO request){

        ClienteEntity cliente = getEntityById(clienteId);
        DireccionEntity direccion = direccionMapper.toEntity(request);

        cliente.getDirecciones().add(direccion);
        direccion.getClientes().add(cliente);
        direccionService.saveEntity(direccion);

        return mapper.toResponse(repository.save(cliente));
    }

    // Desvincular la direccion de un cliente por ID
    public ClienteResponseDTO dissociateDireccion(Long clienteId, Long direccionId) {
        ClienteEntity cliente = getEntityById(clienteId);

        direccionService.getEntityById(direccionId);

        if (!isAssociated(cliente, direccionId)) {
            throw new ResourceNotAssociatedException("La direccion no esta asociada al cliente");
        }

        //Recorre las direcciones del cliente y elimina solo si se encuentra el mismo ID que "direccionId"
        cliente.getDirecciones().removeIf(d -> d.getId().equals(direccionId));

        return mapper.toResponse(repository.save(cliente));
    }

    ///----- Validations -----

    public boolean isAssociated(ClienteEntity cliente, Long direccionId){
        // "anyMatch" devuelve un boolean, y sirve para saber si en la lista de direcciones ya existe una con ese ID
        return cliente.getDirecciones().stream()
                .anyMatch(d -> d.getId().equals(direccionId));
    }

}
