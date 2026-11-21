package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Mapper.AlimentoMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento.AlimentoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento.AlimentoUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AlimentoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.AlimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service de Alimento. La edicion, la baja y la busqueda por id las hereda de ProductoBaseService.
@Service
public class AlimentoService extends ProductoBaseService<AlimentoEntity, AlimentoUpdateRequestDTO, AlimentoResponseDTO> {

    private final AlimentoMapper mapper;
    private final AlimentoRepository alimentoRepository;

    public AlimentoService(AlimentoRepository repository, AlimentoMapper mapper) {
        super(repository);
        this.alimentoRepository = repository;
        this.mapper = mapper;
    }

    public AlimentoResponseDTO createAlimento(AlimentoCreateRequestDTO request){
        AlimentoEntity newAlimento = mapper.toEntity(request);
        return mapper.toResponse(repository.save(newAlimento));
    }

    public AlimentoResponseDTO getDTOById(Long id){
        return mapper.toResponse(getEntityById(id));
    }

    public List<AlimentoResponseDTO> getAll(){
        return alimentoRepository.findByActivoTrue().stream()
                .map(mapper::toResponse)
                .toList();
    }

    // Delega en el Template Method
    public AlimentoResponseDTO updateAlimento(AlimentoUpdateRequestDTO request, Long id){
        return update(request, id);
    }

    ///----- Lo propio de alimento -----

    @Override
    protected void updateSpecificFields(AlimentoEntity entity, AlimentoUpdateRequestDTO request){
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

    @Override
    protected AlimentoResponseDTO toResponse(AlimentoEntity entity) {
        return mapper.toResponse(entity);
    }

    @Override
    protected String nombreTipo() {
        return "alimento";
    }
}
