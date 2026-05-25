package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.AlimentoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AlimentoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlimentoMapper {

    public AlimentoEntity toEntity(AlimentoCreateRequestDTO request);

    public AlimentoResponseDTO toResponse(AlimentoEntity entity);
}
