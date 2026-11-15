package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.EnumMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.PrecioOfertaHelper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Alimento.AlimentoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.AlimentoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EnumMapper.class}, imports = {PrecioOfertaHelper.class})
public interface AlimentoMapper {

    public AlimentoEntity toEntity(AlimentoCreateRequestDTO request);

    @Mapping(target = "precioOferta", expression = "java(PrecioOfertaHelper.calcularPrecioOferta(entity))")
    public AlimentoResponseDTO toResponse(AlimentoEntity entity);
}
