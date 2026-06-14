package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyUpdateFieldException;
import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceAlreadyExistsException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.ProveedorMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Proveedor.ProveedorCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Proveedor.ProveedorUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ProveedorResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.ProveedorEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProveedorService {

    private final ProveedorRepository repository;
    private final ProveedorMapper mapper;

    public ProveedorResponseDTO createProveedor(ProveedorCreateRequestDTO request){
        ProveedorEntity newProveedor = mapper.toEntity(request);
        return mapper.toResponse(repository.save(newProveedor));
    }

    public ProveedorEntity getEntityById(Long id){
        Optional<ProveedorEntity> entity = repository.findById(id);
        if(entity.isPresent())
            return entity.get();
        else throw new ResourceNotFoundException("El Proveedor con la ID: "  + id +  " no existe");
    }

    public ProveedorResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<ProveedorResponseDTO> getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /// -------- Updates / Delete  ----------

    public ProveedorResponseDTO updateProveedor(ProveedorUpdateRequestDTO request, Long id){
        ProveedorEntity proveedor = getEntityById(id);

        if (request.getNombre() != null){
            if (request.getNombre().trim().isEmpty()){
                throw new EmptyUpdateFieldException("El nombre del Proveedor no puede estar vacio");
            }
            if (!request.getNombre().equalsIgnoreCase(proveedor.getNombre()) && repository.existsByNombreIgnoreCase(request.getNombre())){
                throw new ResourceAlreadyExistsException("Ya existe un proveedor con ese nombre");
            }
            proveedor.setNombre(request.getNombre());
        }

        if (request.getTelefono() != null) {
            if (request.getTelefono().trim().isEmpty()) {
                throw new EmptyUpdateFieldException("El telefono del proveedor no puede ser solo espacios en blanco");
            }

            if (!request.getTelefono().equals(proveedor.getTelefono()) && repository.existsByTelefono(request.getTelefono())) {
                throw new ResourceAlreadyExistsException("Ya existe un proveedor con ese telefono");
            }

            proveedor.setTelefono(request.getTelefono());
        }

        return mapper.toResponse(repository.save(proveedor));
    }

    public void deleteById(Long id){
        ProveedorEntity entity = getEntityById(id);

        if (entity.getActivo() == false){
            throw new InactiveResourceException("El Proveedor ya fue eliminado");
        }
        entity.setActivo(false);
        repository.save(entity);
    }
}
