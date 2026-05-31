package SSVG.TPFinalDeliveryMascotero.Mapper;


import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.DireccionResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DireccionMapper {

    public DireccionEntity toEntity(DireccionCreateRequestDTO request);

    @Mapping(
            target = "direccionCompleta",
            expression = "java(formatearDireccion(entity))"
    )
    @Mapping(
            target = "pisoDepto",
            expression = "java(formatearPisoDepto(entity))"
    )
    public DireccionResponseDTO toResponse(DireccionEntity entity);

    default String formatearDireccion(DireccionEntity entity) {
        return entity.getCalle() + " " + entity.getNumero();
    }

    default String formatearPisoDepto(DireccionEntity entity) {
        if (entity.getPiso() == null && entity.getDepartamento() == null) {
            return "Sin especificar";
        }
        if (entity.getPiso() == null){
            return "Depto: " + entity.getDepartamento();
        }
        return "Piso: " + entity.getPiso() + " | Depto: " + entity.getDepartamento();
    }


}