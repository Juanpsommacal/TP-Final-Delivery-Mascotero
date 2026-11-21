package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.InvalidWeightRangeException;
import SSVG.TPFinalDeliveryMascotero.Mapper.AntipulgasMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AntipulgasResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAntipulgas;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.AntipulgasRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service de Antipulgas. La edicion, la baja y la busqueda por id las hereda de ProductoBaseService.
@Service
public class AntipulgasService extends ProductoBaseService<AntipulgasEntity, AntipulgasUpdateRequestDTO, AntipulgasResponseDTO> {

    private final AntipulgasMapper mapper;
    private final AntipulgasRepository antipulgasRepository;

    public AntipulgasService(AntipulgasRepository repository, AntipulgasMapper mapper) {
        super(repository);
        this.antipulgasRepository = repository;
        this.mapper = mapper;
    }

    public AntipulgasResponseDTO createAntipulgas(AntipulgasCreateRequestDTO request){
        if(request.getKgMin() > request.getKgMax())
            throw new InvalidWeightRangeException("El peso minimo no puede ser superior al peso maximo");
        AntipulgasEntity newAntipulgas = mapper.toEntity(request);
        return mapper.toResponse(repository.save(newAntipulgas));
    }

    public AntipulgasResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<AntipulgasResponseDTO> getAll(){
        return antipulgasRepository.findByActivoTrue().stream()
                .map(mapper::toResponse)
                .toList();
    }

    // Delega en el Template Method
    public AntipulgasResponseDTO updateAntipulgas(AntipulgasUpdateRequestDTO request, Long id){
        return update(request, id);
    }

    ///----- Lo propio de antipulgas -----

    @Override
    protected void updateSpecificFields(AntipulgasEntity entity, AntipulgasUpdateRequestDTO request){
        if (request.getTipoAnimal() != null){
            entity.setTipoAnimal(TipoAnimal.valueOf(request.getTipoAnimal().trim().toUpperCase()));
        }
        if (request.getKgMin() != null){
            entity.setKgMin(request.getKgMin());
        }
        if (request.getKgMax() != null){
            entity.setKgMax(request.getKgMax());
        }
        if (request.getTipoAntipulgas() != null){
            entity.setTipoAntipulgas(TipoAntipulgas.valueOf(request.getTipoAntipulgas().trim().toUpperCase()));
        }
    }

    // Gancho: solo antipulgas tiene un rango de peso que validar
    @Override
    protected void validateSpecificRules(AntipulgasEntity entity){
        if (entity.getKgMin() != null && entity.getKgMax() != null
                && entity.getKgMin() > entity.getKgMax()){
            throw new InvalidWeightRangeException("El peso minimo no puede ser superior al peso maximo");
        }
    }

    @Override
    protected AntipulgasResponseDTO toResponse(AntipulgasEntity entity) {
        return mapper.toResponse(entity);
    }

    @Override
    protected String nombreTipo() {
        return "antipulgas";
    }
}
