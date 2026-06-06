package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.DetalleCompraResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DetalleCompraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface DetalleCompraMapper {

    @Mapping(
            target = "precioTotal",
            expression = "java(calcularPrecioTotal(entity))"
    )
    @Mapping(target = "nombreProducto", source = "producto.nombre")
    @Mapping(target = "idProducto", source = "producto.id")
    public DetalleCompraResponseDTO toResponse (DetalleCompraEntity entity);

    default BigDecimal calcularPrecioTotal(DetalleCompraEntity entity) {
        return entity.getPrecioUnitario().multiply(BigDecimal.valueOf(entity.getCantidad()));
    }
}
