package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(
            target = "tipoProducto",
            expression = "java(getTipoProducto(entity))"
    )
    @Mapping(
            target = "precioFinalConDesc",
            expression = "java(calcularPrecioFinal(entity))"
    )
    ProductoResponseDTO toResponse(ProductoEntity entity);

    default String getTipoProducto(ProductoEntity entity) {

        if (entity instanceof AlimentoEntity) {
            return "ALIMENTO";
        }

        if (entity instanceof AntipulgasEntity) {
            return "ANTIPULGAS";
        }

        return "DESCONOCIDO";
    }


    default Double calcularPrecioFinal(ProductoEntity entity) {

        if (entity.getPrecio() == null) {
            return 0.0;
        }

        BigDecimal precio = entity.getPrecio();

        if (entity.getOferta() == null || entity.getOferta().getPorcentaje() == null) {
            return precio.doubleValue();
        }

        BigDecimal porcentaje = BigDecimal.valueOf(entity.getOferta().getPorcentaje());

        BigDecimal descuento = precio
                .multiply(porcentaje)
                .divide(BigDecimal.valueOf(100));

        BigDecimal resultado = precio.subtract(descuento);

        return resultado.doubleValue();
    }
}