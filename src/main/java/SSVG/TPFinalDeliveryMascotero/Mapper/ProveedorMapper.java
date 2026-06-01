package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.ProveedorCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.ProveedorResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.ProveedorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProveedorMapper {

    public ProveedorEntity toEntity(ProveedorCreateRequestDTO request);

    public ProveedorResponseDTO toResponse(ProveedorEntity entity);
}
