package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.EnumMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.PedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {DetallePedidoMapper.class, DireccionMapper.class, PagoMapper.class})
public interface PedidoMapper {

    public PedidoEntity toEntity(PedidoCreateRequestDTO request);

    @Mapping(target = "nombreCliente",
            expression = "java(getNombreClienteCompleto(entity))")
    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "telefonoCliente", source = "cliente.telefono")
    @Mapping(target = "detallePedido", source = "productos")
    @Mapping(target = "direccionCompleta", expression = "java(formatearDireccionCompleta(entity))")
    @Mapping(target = "alcanzaStock", expression = "java(alcanzaStock(entity))")
    //Estos 2 Mappings los tuve que hacer manual porque no funcionan. No se por que
    @Mapping(target = "pisoDepto", source = "pisoDepto")
    @Mapping(target = "id", source = "id")
    public PedidoResponseDTO toResponse (PedidoEntity entity);

    default String getNombreClienteCompleto(PedidoEntity entity){
        return entity.getCliente().getNombre() + " " + entity.getCliente().getApellido();
    }

    default boolean alcanzaStock(PedidoEntity entity) {
        return entity.getEstadoPedido() == EstadoPedido.FALTA_MERCADERIA
                && entity.getProductos().stream().allMatch(detalle -> detalle.getProducto().getStock() >= detalle.getCantidad());
    }

    default String formatearDireccionCompleta(PedidoEntity entity) {
        return entity.getCalle() + " " + entity.getNumero();
    }
}
