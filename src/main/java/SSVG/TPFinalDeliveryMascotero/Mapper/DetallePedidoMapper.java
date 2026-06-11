package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.DetallePedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DetallePedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface DetallePedidoMapper {

    @Mapping(
            target = "precioTotal" ,
            expression = "java(calcularPrecioTotal(entity))"
    )
    @Mapping(
            target = "nombreProducto",
            expression = "java(getNombreProductoCompleto(entity))"
    )
    @Mapping(
            target = "subtotal",
            expression = "java(calcularSubTotal(entity))"
    )
    @Mapping(
            target = "descuentoAplicado",
            expression = "java(entity.getDescuentoAplicado() != null ? entity.getDescuentoAplicado() : 0.0)"
    )
    public DetallePedidoResponseDTO toResponse (DetallePedidoEntity entity);

    default BigDecimal calcularPrecioTotal(DetallePedidoEntity entity) {
        BigDecimal precioTotal;
        precioTotal = entity.getPrecioUnitario().multiply(BigDecimal.valueOf(entity.getCantidad()));
        if(entity.getDescuentoAplicado() != null){
            precioTotal = precioTotal.multiply(BigDecimal.valueOf(1.00 - entity.getDescuentoAplicado() / 100));
        }
        return precioTotal;
    }

    default BigDecimal calcularSubTotal(DetallePedidoEntity entity){
        return entity.getPrecioUnitario().multiply(BigDecimal.valueOf(entity.getCantidad()));
    }

    default String getNombreProductoCompleto(DetallePedidoEntity entity){
        return entity.getProducto().getMarca() + " " + entity.getProducto().getNombre();
    }
}
