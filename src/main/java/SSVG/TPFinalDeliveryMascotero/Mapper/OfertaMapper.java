package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Oferta.OfertaCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoOfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfertaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    OfertaEntity toEntity(OfertaCreateRequestDTO request);

    @Mapping(target = "productos", expression = "java(mapProductos(entity))")
    OfertaResponseDTO toResponse(OfertaEntity entity);

    List<OfertaResponseDTO> toResponseDTOList(List<OfertaEntity> ofertas);

    default List<ProductoOfertaResponseDTO> mapProductos(OfertaEntity entity){
        if (entity.getProductos() == null){
            return List.of();
        }

        return entity.getProductos().stream()
                .map(producto -> new ProductoOfertaResponseDTO(
                        producto.getId(), producto.getNombre()
                ))
                .toList();
    }

}

