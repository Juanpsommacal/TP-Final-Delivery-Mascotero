package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.EnumMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pago.PagoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PagoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.PagoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EnumMapper.class})
public interface PagoMapper {

    public PagoEntity toEntity(PagoCreateRequestDTO request);

    @Mapping(target = "pedidoId", source = "pedido.id")
    @Mapping(target = "clienteId", source = "pedido.cliente.id")
    // Creo q esto no va. hay q Probarlo @Mapping(target = "metodoPago", source = "metodoPago")
    public PagoResponseDTO toResponse(PagoEntity entity);
}
