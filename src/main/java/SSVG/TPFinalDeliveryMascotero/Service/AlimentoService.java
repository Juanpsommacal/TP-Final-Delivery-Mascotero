package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyUpdateFieldException;
import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.AlimentoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento.AlimentoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento.AlimentoUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AlimentoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
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
        else throw new ResourceNotFoundException("El alimento con la ID: " + id + " no existe.");
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

    public AlimentoResponseDTO updateAlimento(AlimentoUpdateRequestDTO request, Long id){
        AlimentoEntity entity = getEntityById(id);

        // Corroboro que el Alimento este Activo
        if (!entity.getActivo()){
            throw new InactiveResourceException("El producto esta dado de baja");
        }

        updateCommonFields(entity, request);
        updateSpecificFields(entity, request);

        return mapper.toResponse(repository.save(entity));
    }

    ///----- Validations -----

    private void updateCommonFields(AlimentoEntity entity, AlimentoUpdateRequestDTO request){
        if (request.getNombre() != null) {
            if(request.getNombre().trim().isEmpty()){
                throw new EmptyUpdateFieldException("El nombre puede ser solo espacios en blanco");
            }
            entity.setNombre(request.getNombre());
        }

        if (request.getDescripcion() != null) {
            if (request.getDescripcion().trim().isEmpty()){
                throw new EmptyUpdateFieldException("La descripcion no puede ser solo espacios en blanco");
            }
            entity.setDescripcion(request.getDescripcion());
        }

        if (request.getPrecio() != null) {
            entity.setPrecio(request.getPrecio());
        }

        if (request.getStock() != null) {
            entity.setStock(request.getStock());
        }

        if (request.getMarca() != null) {
            if (request.getMarca().trim().isEmpty()){
                throw new EmptyUpdateFieldException("La marca no puede ser solo espacios en blanco");
            }
            entity.setMarca(request.getMarca());
        }
    }

    private void updateSpecificFields(AlimentoEntity entity, AlimentoUpdateRequestDTO request){
        if (request.getPeso() != null){
            entity.setPeso(request.getPeso());
        }
        if (request.getUnidadMedida() != null){
            entity.setUnidadMedida(UnidadMedida.valueOf(request.getUnidadMedida().trim().toUpperCase()));
        }
        if (request.getEtapaVida() != null){
            entity.setEtapaVida(EtapaVida.valueOf(request.getEtapaVida().trim().toUpperCase()));
        }
        if (request.getTipoAnimal() != null){
            entity.setTipoAnimal(TipoAnimal.valueOf(request.getTipoAnimal().trim().toUpperCase()));
        }
    }


}
