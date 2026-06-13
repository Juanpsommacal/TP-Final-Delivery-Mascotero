package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {DetallePedidoMapper.class})
public interface PedidoMapper {

    public PedidoEntity toEntity(PedidoCreateRequestDTO request);

    @Mapping(target = "nombreCliente",
            expression = "java(getNombreClienteCompleto(entity))")
    @Mapping(target = "detallePedido", source = "productos")
    @Mapping(target = "direccionCompleta", source = "direccionCompleta")
    @Mapping(target = "pisoDepto", source = "pisoDepto")
    public PedidoResponseDTO toResponse (PedidoEntity entity);

    default String getNombreClienteCompleto(PedidoEntity entity){
        return entity.getCliente().getNombre() + " " + entity.getCliente().getApellido();
    }
}
