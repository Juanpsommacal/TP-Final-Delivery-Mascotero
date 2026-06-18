package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.DetallePedidoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DetallePedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring")
public interface DetallePedidoMapper {

//    @Mapping(
//            target = "precioTotal" ,
//            expression = "java(calcularPrecioTotal(entity))"
//    )
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

    // Se calcula el Precio final que le va a quedar al producto dependiendo si tiene o no una oferta asignada
    default BigDecimal calcularPrecioFinalProducto(DetallePedidoEntity entity) {
        BigDecimal precioOriginal = entity.getPrecioUnitario();

        if(entity.getDescuentoAplicado() == null || entity.getDescuentoAplicado() <= 0){
            return precioOriginal;
        }

        BigDecimal porcentaje = BigDecimal.valueOf(entity.getDescuentoAplicado());

        BigDecimal descuento = precioOriginal.multiply(porcentaje.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        return precioOriginal.subtract(descuento);
    }

    default BigDecimal calcularSubTotal(DetallePedidoEntity entity){
        return calcularPrecioFinalProducto(entity).multiply(BigDecimal.valueOf(entity.getCantidad()).setScale(2, RoundingMode.HALF_UP));
    }

    default String getNombreProductoCompleto(DetallePedidoEntity entity){
        return entity.getProducto().getMarca() + " " + entity.getProducto().getNombre();
    }
}
