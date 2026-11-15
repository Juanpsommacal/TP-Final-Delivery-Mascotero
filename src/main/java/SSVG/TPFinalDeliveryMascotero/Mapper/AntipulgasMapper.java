package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.EnumMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.PrecioOfertaHelper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AntipulgasResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EnumMapper.class}, imports = {PrecioOfertaHelper.class})
public interface AntipulgasMapper {

    public AntipulgasEntity toEntity(AntipulgasCreateRequestDTO request);

    @Mapping(target = "precioOferta", expression = "java(PrecioOfertaHelper.calcularPrecioOferta(entity))")
    public AntipulgasResponseDTO toResponse(AntipulgasEntity entity);
}
