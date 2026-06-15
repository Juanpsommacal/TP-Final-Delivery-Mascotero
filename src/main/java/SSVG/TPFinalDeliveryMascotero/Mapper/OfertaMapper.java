package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Oferta.OfertaCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoOfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductoOfertaMapper.class})
public interface OfertaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    OfertaEntity toEntity(OfertaCreateRequestDTO request);

    @Mapping(target = "productos", source = "productos")
    OfertaResponseDTO toResponse(OfertaEntity entity);

}

