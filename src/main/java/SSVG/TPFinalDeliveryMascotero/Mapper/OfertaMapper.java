package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Oferta.OfertaCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoOfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OfertaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    OfertaEntity toEntity(OfertaCreateRequestDTO request);

    @Mapping(target = "productos", expression = "java(mapProductos(entity))")
    OfertaResponseDTO toResponse(OfertaEntity entity);

    default List<ProductoOfertaResponseDTO> mapProductos(OfertaEntity entity){
        if (entity.getProductos() == null){
            return List.of();
        }

        return entity.getProductos().stream()
                .map(producto -> {

                    BigDecimal precioConDescuento =
                            producto.getPrecio().multiply(
                                    BigDecimal.ONE.subtract(
                                            entity.getPorcentaje()
                                                    .divide(BigDecimal.valueOf(100))
                                    )
                            );

                    return new ProductoOfertaResponseDTO(
                            producto.getId(),
                            producto.getNombre(),
                            precioConDescuento
                    );
                })
                .toList();
    }

}

