package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.EnumMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AntipulgasResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EnumMapper.class})
public interface AntipulgasMapper {

    public AntipulgasEntity toEntity(AntipulgasCreateRequestDTO request);

    public AntipulgasResponseDTO toResponse(AntipulgasEntity entity);
}
