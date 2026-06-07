package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {DetallePedidoMapper.class, DireccionMapper.class})
public interface PedidoMapper {

    public PedidoEntity toEntity(PedidoCreateRequestDTO request);

    @Mapping(target = "nombreCliente",
            expression = "java(getNombreClienteCompleto(entity))")
    @Mapping(target = "direccion", source = "direccion")
    @Mapping(target = "detallePedido", source = "productos")
    public PedidoResponseDTO toResponse (PedidoEntity entity);

    default String getNombreClienteCompleto(PedidoEntity entity){
        return entity.getCliente().getNombre() + " " + entity.getCliente().getApellido();
    }
}
