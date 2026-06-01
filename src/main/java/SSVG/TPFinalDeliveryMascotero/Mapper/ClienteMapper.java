package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Cliente.ClienteCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ClienteResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DireccionMapper.class)
public interface ClienteMapper {

    public ClienteEntity toEntity(ClienteCreateRequestDTO request);

    public ClienteResponseDTO toResponse(ClienteEntity entity);
}
