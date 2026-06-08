package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra.OfertaRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfertaMapper {

    OfertaEntity toEntity(OfertaRequestDTO requestDTO);

    OfertaResponseDTO toResponse(OfertaEntity entity);

    List<OfertaResponseDTO> toResponseDTOList(List<OfertaEntity> ofertas);
}