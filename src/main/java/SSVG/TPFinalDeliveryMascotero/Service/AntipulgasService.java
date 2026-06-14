package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyUpdateFieldException;
import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.InvalidWeightRangeException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.AntipulgasMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasUpdateRequestDTO;
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
        else throw new ResourceNotFoundException("El antipulgas con la ID: " + id + " no existe");
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

    ///----- Updates -----

    public AntipulgasResponseDTO updateAntipulgas(AntipulgasUpdateRequestDTO request, Long id){
        AntipulgasEntity entity = getEntityById(id);

        // Corroboro que el Antipulgas este Activo
        if (!entity.getActivo()){
            throw new InactiveResourceException("El producto de tipo antipulgas esta dado de baja");
        }
        // Llamo a los metodos para Validar cada dato recibido por el Request
        updateCommonFields(entity, request);
        updateSpecificFields(entity, request);
        validateWeightRange(entity);

        return mapper.toResponse(repository.save(entity));
    }

    ///----- Validations -----

    // Validaciones de los campos comunes del Producto, en este caso Antipulgas
    private void updateCommonFields(AntipulgasEntity entity, AntipulgasUpdateRequestDTO request){
        // validacion del Nombre
        if (request.getNombre() != null){
            if (request.getNombre().trim().isEmpty()){
                throw new EmptyUpdateFieldException("La nombre no puede ser solo con espacios en blanco");
            }
            entity.setNombre(request.getNombre());
        }

        // validacion de la Descripcion
        if (request.getDescripcion() != null){
            if (request.getDescripcion().trim().isEmpty()){
                throw new EmptyUpdateFieldException("La descripcion no puede ser solo espacios en blanco");
            }
            entity.setDescripcion(request.getDescripcion());
        }

        // Validacion del Precio
        if (request.getPrecio() != null){
            entity.setPrecio(request.getPrecio());
        }

        // Validacion del Stock
        if (request.getStock() != null){
            entity.setStock(request.getStock());
        }

        // Validacion de la Marca
        if (request.getMarca() != null){
            if (request.getMarca().trim().isEmpty()){
                throw new EmptyUpdateFieldException("La marca no puede solo ser espacios en blanco");
            }
            entity.setMarca(request.getMarca());
        }
    }

    // Validaciones de los campos especificos y unicos de Antipulgas
    private void updateSpecificFields(AntipulgasEntity entity, AntipulgasUpdateRequestDTO request){
        // Validacion de Tipo de Animal
        if (request.getTipoAnimal() != null){
            entity.setTipoAnimal(request.getTipoAnimal());
        }
        // Validacion de el Peso Minimo recibido
        if (request.getKgMin() != null){
            entity.setKgMin(request.getKgMin());
        }
        // Validacion del Peso Maximo recibido
        if (request.getKgMax() != null){
            entity.setKgMax(request.getKgMax());
        }
        // Validacion del Tipo de Antipulgas
        if (request.getTipoAntipulgas() != null){
            entity.setTipoAntipulgas(request.getTipoAntipulgas());
        }
    }

    // Aca se valida el Rango de los pesos del Antipulgas
    private void validateWeightRange(AntipulgasEntity entity){
        // Primero valido que me lleguen los datos de los pesos (MIN y MAX)
        // y despues me aseguro que el peso minimo no sea mas grande que el maximo
        if (entity.getKgMin() != null && entity.getKgMax() != null
            && entity.getKgMin() > entity.getKgMax()){

            throw new InvalidWeightRangeException("El peso minimo no puede ser superior al peso maximo");
        }
    }

}
