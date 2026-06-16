package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoOfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ProductoOfertaMapper {

    @Mapping(
            target = "marcaYNombre",
            expression = "java(getMarcaYNombre(entity))"
    )
    @Mapping(
            target = "precioConDescuento",
            expression = "java(calcularPrecioConDescuento(entity))"
    )
    public ProductoOfertaResponseDTO toResponse(ProductoEntity entity);

    default String getMarcaYNombre(ProductoEntity entity){
        return entity.getMarca() + " " + entity.getNombre();
    }

    default BigDecimal calcularPrecioConDescuento(ProductoEntity entity){
        if(entity.getOferta() == null){
            return entity.getPrecio();
        }

        BigDecimal descuento =
                entity.getPrecio()
                        .multiply(
                                BigDecimal.valueOf(
                                        entity.getOferta()
                                                .getPorcentaje()
                                )
                        )
                        .divide(BigDecimal.valueOf(100));

        return entity.getPrecio().subtract(descuento);
    }
}
