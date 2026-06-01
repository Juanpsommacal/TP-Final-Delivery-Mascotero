package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.ProveedorMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.ProveedorCreateRequestDTO;
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
        else throw new ResourceNotFoundException("El Proveedor no existe");
    }

    public ProveedorResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<ProveedorResponseDTO> getAll(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

}
