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
    public DetalleCompraResponseDTO toResponse (DetalleCompraEntity entity);

    /*Dudoso si tener esto aca o no. Segun lo que investigue si es algo sencillo puede ir en el mapper
    Pero si se complejiza, como por ejemplo si agregamos un subtotal y descuentos, IVA, etc.
    Habria que ponerlo en el service
    */
    default BigDecimal calcularPrecioTotal(DetalleCompraEntity entity) {
        return entity.getPrecioUnitario().multiply(BigDecimal.valueOf(entity.getCantidad()));
    }
}
