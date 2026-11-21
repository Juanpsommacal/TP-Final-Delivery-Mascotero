package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceAlreadyExistsException;
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

import java.math.BigDecimal;
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
        validarNoDuplicado(newAlimento);
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

    // Gancho del Template Method: al editar tampoco puede quedar igual a otro alimento
    @Override
    protected void validateSpecificRules(AlimentoEntity entity) {
        validarNoDuplicado(entity);
    }

    ///----- Duplicados -----

    // Un alimento es duplicado si ya hay otro activo con la misma marca, nombre, animal, etapa de vida y peso.
    // Cambiando cualquiera de esos datos se puede cargar (por ejemplo, la misma comida en 3 kg y en 15 kg).
    // Los dados de baja no cuentan: no se pueden reactivar, asi que hay que poder cargarlos de nuevo.
    private void validarNoDuplicado(AlimentoEntity alimento) {
        String marca = normalizar(alimento.getMarca());
        String nombre = normalizar(alimento.getNombre());
        BigDecimal gramos = enGramos(alimento);

        boolean existe = alimentoRepository
                .findByActivoTrueAndTipoAnimalAndEtapaVida(alimento.getTipoAnimal(), alimento.getEtapaVida())
                .stream()
                .filter(otro -> !otro.getId().equals(alimento.getId()))
                .anyMatch(otro -> normalizar(otro.getMarca()).equals(marca)
                        && normalizar(otro.getNombre()).equals(nombre)
                        && enGramos(otro).compareTo(gramos) == 0);

        if (existe) {
            throw new ResourceAlreadyExistsException("Ya existe el alimento " + alimento.getMarca().trim() + " "
                    + alimento.getNombre().trim() + " de " + presentacion(alimento));
        }
    }

    // Sin mayusculas ni espacios de mas: "  CatPro   Gato " es igual a "catpro gato"
    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    // 15 kg y 15000 g son el mismo peso
    private BigDecimal enGramos(AlimentoEntity alimento) {
        BigDecimal peso = BigDecimal.valueOf(alimento.getPeso());
        return alimento.getUnidadMedida() == UnidadMedida.KILOGRAMOS
                ? peso.multiply(BigDecimal.valueOf(1000)).stripTrailingZeros()
                : peso.stripTrailingZeros();
    }

    private String presentacion(AlimentoEntity alimento) {
        String unidad = alimento.getUnidadMedida() == UnidadMedida.GRAMOS ? "g" : "kg";
        return BigDecimal.valueOf(alimento.getPeso()).stripTrailingZeros().toPlainString().replace('.', ',') + " " + unidad;
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
