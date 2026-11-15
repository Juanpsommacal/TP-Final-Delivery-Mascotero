package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.EnumMapper;
import SSVG.TPFinalDeliveryMascotero.Mapper.Helper.PrecioOfertaHelper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.Producto.ProductoResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.UnidadMedida;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {PrecioOfertaHelper.class})
public interface ProductoMapper {

    @Mapping(
            target = "tipoProducto",
            expression = "java(getTipoProducto(entity))")
    @Mapping(target = "precioOferta", expression = "java(PrecioOfertaHelper.calcularPrecioOferta(entity))")
    @Mapping(target = "presentacion", expression = "java(getPresentacion(entity))")
    public ProductoResponseDTO toResponse(ProductoEntity entity);

    default String getPresentacion(ProductoEntity entity) {
        if (entity instanceof AlimentoEntity alimento) {
            String unidad = alimento.getUnidadMedida() == UnidadMedida.GRAMOS ? "g" : "kg";
            return formatearNumero(alimento.getPeso()) + " " + unidad;
        }

        if (entity instanceof AntipulgasEntity antipulgas) {
            if (antipulgas.getKgMin() <= 0) {
                return "Hasta " + formatearNumero(antipulgas.getKgMax()) + " kg";
            }
            return "De " + formatearNumero(antipulgas.getKgMin()) + " a " + formatearNumero(antipulgas.getKgMax()) + " kg";
        }

        return null;
    }

    // 3.0 -> "3" y 1.5 -> "1,5"
    default String formatearNumero(Double valor) {
        return valor % 1 == 0 ? String.valueOf(valor.longValue()) : String.valueOf(valor).replace('.', ',');
    }

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
