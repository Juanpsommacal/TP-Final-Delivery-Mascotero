package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(
            target = "tipoProducto",
            expression = "java(getTipoProducto(entity))")
    public ProductoResponseDTO toResponse(ProductoEntity entity);

    default String getTipoProducto(ProductoEntity entity) {

        if (entity instanceof AlimentoEntity) {
            return "ALIMENTO";
        }

        if (entity instanceof AntipulgasEntity) {
            return "ANTIPULGAS";
        }

        return "DESCONOCIDO";
    }
}
